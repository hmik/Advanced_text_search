package com.hmik.gui;

import com.hmik.QueryBuilder;
import com.hmik.Movie;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class Movies {
    private JButton buttonSearch;
    public JPanel panelMain;
    private JComboBox queryInput;
    private JRadioButton radioAND;
    private JRadioButton radioOR;
    private JLabel queryLabel;
    private JLabel btnLabel;
    private JTabbedPane tabbedPane1;
    private JTextArea textArea1;
    private JTextPane results;
    private JScrollPane sp;
    private JLabel resultsLabel;
    private JPanel inputPanel;
    private JLabel titleLabel;
    private JTextField categoriesInput;
    private JLabel categoriesLabel;
    private JTextArea sumaryInput;
    private JTextField titleInput;
    private JLabel summarLabel;
    private JTextArea descriptionInput;
    private JLabel descriptionLabel;
    private JButton addMovieButton;
    private JCheckBox autocomplete;
    private JComboBox suggestionInput;

    public Movies() {

        descriptionInput.setLineWrap(true);
        sumaryInput.setLineWrap(true);
        textArea1.setLineWrap(true);


        buttonSearch.addActionListener(actionEvent -> {
            String q = ((JTextComponent)queryInput.getEditor().getEditorComponent()).getText();
            if( !q.equals("")){
                List<Movie> movies = QueryBuilder.query(q, radioAND.isSelected());
                textArea1.setText(QueryBuilder.getSQLText());

                int counter = 1;
                results.setText("");
                if( movies.isEmpty()){
                    results.setText("No results found");
                }else {
//                    results.setText("<html>");
//                    for (Movie m : movies) {
//                        results.setText(results.getText() + "<span>" + counter++ + ". </span>");
//                        results.setText(results.getText() + "<span>" + m.toString() + "</span>");
//                        results.setText(results.getText() + "\n-----------------------------------------\n");
//
//
//                    }
//                    results.setText(results.getText() + "</html>");

                    StringBuilder buildSomething = new StringBuilder();
                    for (Movie m : movies){
                        buildSomething.append( "<span>" + counter++ + ". ");
                        buildSomething.append(m.toString() + "<br>");
                        buildSomething.append("------------------------------------------------------------<br></span>");
                    }
                    results.setContentType("text/html");
                    results.setText(buildSomething.toString());

                }
            }


        });
        addMovieButton.addActionListener(actionEvent -> {
            Movie mv = new Movie();
            mv.setName(titleInput.getText());
            mv.setCategory(categoriesInput.getText());
            mv.setSummary(sumaryInput.getText());
            mv.setDescription(descriptionInput.getText());
            String msg = QueryBuilder.add(mv);

            JOptionPane.showMessageDialog(null, msg);

            titleInput.setText("");
            categoriesInput.setText("");
            sumaryInput.setText("");
            descriptionInput.setText("");

        });
        queryInput.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if( autocomplete.isSelected()) {
                    String q = ((JTextComponent) queryInput.getEditor().getEditorComponent()).getText();
                    List<String> suggestions = QueryBuilder.getSuggestion(q);
                    int index = 0;
                    int size = queryInput.getItemCount();


                    if (e.getKeyCode() == 13){
                        queryInput.hidePopup();
                        return;
                    }
                    if (e.getKeyCode() == 40) {
                        queryInput.setSelectedIndex((index++) % size);
                        ((JTextComponent) queryInput.getEditor().getEditorComponent()).setText(queryInput.getSelectedItem().toString());


                    } else if (e.getKeyCode() == 38) {
                        queryInput.setSelectedIndex((index--) % size);
                        ((JTextComponent) queryInput.getEditor().getEditorComponent()).setText(queryInput.getSelectedItem().toString());

                    }else if (e.getKeyCode() == 13) {
                        queryInput.hidePopup();
                    } else {
                        if (!suggestions.isEmpty()) {
                            queryInput.removeAllItems();
                            queryInput.hidePopup();
                            queryInput.addItem(q);
                            for (String s : suggestions) {
                                queryInput.addItem(s);
                            }
                            queryInput.showPopup();

                        }
                    }
                }
            }

        });
    }
}
