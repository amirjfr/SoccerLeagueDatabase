package ui;

import delegates.OperationTabDelegate;
import javafx.util.Pair;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationTab extends JPanel {
    private JScrollPane scrollPane;
    private JPanel inputPanel;
    private JComboBox<CustomQuery> tabComboBox;
    private JButton submit;
    private OperationTabDelegate delegate = null;
    private final Color MLS_BLUE = new Color(0, 31, 91);
    private final Color MLS_WHITE = new Color(255, 255, 255);

    public OperationTab(List<CustomQuery> queries) {
        super();

        this.setLayout(new GridBagLayout());
        inputPanel = new JPanel();
        tabComboBox = new JComboBox<>();
        submit = new JButton();

        this.setBackground(MLS_BLUE);
        inputPanel.setBackground(MLS_BLUE);
        submit.setText("Submit");

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.insets = new Insets(5, 0, 5, 0);

        JLabel selectQueryLabel = new JLabel("Select a function:");
        selectQueryLabel.setForeground(MLS_WHITE);
        selectQueryLabel.setFont(new Font(selectQueryLabel.getFont().getName(), Font.PLAIN, 24));
        constraints.gridy = 0;
        constraints.gridheight = 1;
        this.add(selectQueryLabel, constraints);

        queries.stream().forEachOrdered(tabComboBox::addItem); // add to drop-down
        tabComboBox.setSelectedIndex(-1);
        constraints.gridy = 1;
        constraints.gridheight = 1;
        this.add(tabComboBox, constraints);

        GridLayout gridLayout = new GridLayout(10, 2, 20, 5);
        inputPanel.setLayout(gridLayout);
        inputPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        constraints.gridy = 2;
        constraints.gridheight = 10;
        this.add(inputPanel, constraints);

        constraints.gridy = 13;
        constraints.gridheight = 1;
        this.add(submit, constraints);

        JLabel queryResultLabel = new JLabel("Result:");
        queryResultLabel.setForeground(MLS_WHITE);
        queryResultLabel.setFont(new Font(queryResultLabel.getFont().getName(), Font.PLAIN, 18));
        constraints.gridy = 14;
        constraints.gridheight = 1;
        this.add(queryResultLabel, constraints);

        scrollPane = new JScrollPane();
        scrollPane.setPreferredSize(new Dimension(900, 150));
        constraints.gridy = 15;
        constraints.gridheight = 10;
        this.add(scrollPane, constraints);

        // update UI on selection
        tabComboBox.addActionListener(e -> {
            Object selected = tabComboBox.getSelectedItem();
            inputPanel.removeAll();

            if (selected instanceof CustomQuery) {
                CustomQuery query = (CustomQuery) selected;
                String[] arguments = query.getUiArguments();
                System.out.println(query);

                gridLayout.setRows(arguments.length);
                inputPanel.validate();
                setInputs(arguments);
            } else {
                inputPanel.updateUI();
            }
        });

        submit.addActionListener(e -> {
            onSubmitClick((CustomQuery) tabComboBox.getSelectedItem());
        });

        this.setVisible(true);
    }

    private void setInputs(String[] inputs) {
        for (String input : inputs) {
            JLabel inputName = new JLabel(input);
            JTextField inputTextField = new JTextField("", 25);
            inputName.setForeground(MLS_WHITE);
            inputPanel.add(inputName);
            inputPanel.add(inputTextField);
        }
        inputPanel.updateUI();
    }

    private Map<String, String> getInputs() {
        Map<String, String> inputs = new HashMap<>();
        Component[] inputComponents = inputPanel.getComponents();
        for (int i = 0; i < inputComponents.length; i = i + 2) {
            JLabel inputName = (JLabel) inputComponents[i];
            JTextField inputTextField = (JTextField) inputComponents[i + 1];
            inputs.put(inputName.getText(), inputTextField.getText());
        }
        return inputs;
    }

    private void setMessage(String message) {
        scrollPane.setViewportView(new JLabel(message));
    }

    private void setOutputs(String[] columnNames, List<List<String>> outputs) {
        String[][] data = new String[outputs.size()][];
        for (int i = 0; i < outputs.size(); i++) {
            data[i] = outputs.get(i).toArray(new String[0]);
        }
        JTable resultTable = new JTable(data, columnNames);
        resultTable.setModel(new DefaultTableModel(data, columnNames) {
            // Makes the cells not editable
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        JTableHeader tableHeader = resultTable.getTableHeader();
        tableHeader.setFont(tableHeader.getFont().deriveFont(14.0f).deriveFont(Font.BOLD));
        scrollPane.setViewportView(resultTable);
    }

    public void setDelegate(OperationTabDelegate delegate) {
        this.delegate = delegate;
    }

    private void onSubmitClick(CustomQuery customQuery) {
        if (delegate == null || customQuery == null) return;
        customQuery.setAttributeMap(getInputs());

        switch (customQuery.getQueryType()) {
            case INSERT:
            case UPDATE:
            case DELETE:
                handleDmlQuery(customQuery);
                break;

            case SELECTION:
            case PROJECTION:
            case JOIN:
            case HAVING:
            case DIVISION:
            case GROUPBY:
            case NESTEDGROUPBY:
                handleQuery(customQuery);
                break;

            default:
                System.out.println("Can't handle query type");
        }
    }

    private void handleDmlQuery(CustomQuery customQuery) {
        setMessage(delegate.dmlQuery(customQuery));
    }

    private void handleQuery(CustomQuery customQuery) {
        Pair<String, List<List<String>>> result = delegate.query(customQuery);
        if (result.getValue() == null) {
            setMessage(result.getKey());
        } else {
            setOutputs(customQuery.getColumnNames(), result.getValue());
        }
    }

}
