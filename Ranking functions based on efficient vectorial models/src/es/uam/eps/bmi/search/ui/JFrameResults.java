/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uam.eps.bmi.search.ui;

import es.uam.eps.bmi.search.SearchEngine;
import es.uam.eps.bmi.search.parser.Parser;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingDoc;
import javax.swing.*; 


import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author Javi
 */
public class JFrameResults extends JFrame implements ListSelectionListener,
		ActionListener {
	private  JPanel panel1;
	private  JPanel panel2;
        private  JPanel panel3;
        private  JPanel panel4;

        private JLabel label;


	
        private DefaultTableModel dataModel;
        private InmutableTable table;

        private JButton exitBut; 
        private SearchRanking ranking;
        JScrollPane scrollPane;


        
    public JFrameResults(SearchEngine engine, String query, int cutoff) throws IOException{
            
          
            this.panel1 = new JPanel();
            this.panel2 = new JPanel();
            this.panel3 = new JPanel();
            this.panel4 = new JPanel();

            
            this.label = new JLabel("<html><b>RESULTS</b><html>");


            dataModel = new DefaultTableModel();
            exitBut = new JButton("Come back");
            exitBut.setFocusable(false);
            exitBut.addActionListener(this);


            //exitBut.setBounds(200, 10, 30, 10);
           
            //panel3.setLayout(new GridBagLayout());
            
           
            
            panel3.setLayout(new BorderLayout());



            ranking = engine.search(query, 5);
            table= new InmutableTable(dataModel);
            scrollPane = new JScrollPane(table);
            
            showResults();
	}
    private void showResults() throws IOException{
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.setSize(700, 300);
		Object[] titles = { "Score", "Document"};
		
		this.getContentPane().setLayout(new BorderLayout());
                
                //scrollPane.setVisible(false);
		dataModel.setNumRows(0);
		dataModel.setColumnIdentifiers(titles);
		
		
		for (SearchRankingDoc result : ranking){
                    
				
                    Object[] newRow = { result.getScore(),result.getPath()};
                    if(result.getScore() >0 ){
                        dataModel.addRow(newRow);

                    }
				      
                }
               
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		 double heightRows = table.getRowHeight()*table.getRowCount();
                double spacing = table.getIntercellSpacing().height * (table.getRowCount()-1);
                double heightHeader = table.getTableHeader().getHeight();
                

                double  scrollPaneHeight = scrollPane.getInsets().top + scrollPane.getInsets().bottom;
                
                double total = heightRows +spacing +heightHeader +scrollPaneHeight; 
                //table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                table.setPreferredScrollableViewportSize(new Dimension(600, (int) total));
                	table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				
           //return sb.toString().trim();
				JTable table = (JTable) me.getSource();
				Point p = me.getPoint();
				int row = table.rowAtPoint(p);
				String doc= (String) table.getValueAt(row, 1);
				
				
				if (me.getClickCount() == 2 || me.getClickCount() == 3) {
                                    scrollPane.setVisible(false);
                                    try {
                                    //TODO change for the next delivery with pdf and normal files 
                                   if(doc.startsWith("http") || doc.endsWith(".html")){
                                       
                                            StringBuilder sb = new StringBuilder();
                                            Document document = Jsoup.connect(doc).get();
                                           URL oracle = new URL(doc);
                                            BufferedReader in = new BufferedReader( new InputStreamReader(oracle.openStream()));

                                        String inputLine;
                                        while ((inputLine = in.readLine()) != null){
                                            System.out.println(inputLine);

                                        }
                                        List<Element> elements = new ArrayList<>();
                                        elements.addAll( document.select("h1, h2, h3, li, p"));
                                        
                                            elements.forEach((element) -> {
                                                /*
                                                * element.text() returns the text of this element (= without tags).
                                                */
                                                sb.append(element.text()).append('\n');
                                            });
                                        JTextArea jta =new JTextArea (sb.toString());
                                           JScrollPane scrollPane=  new JScrollPane(jta){
                                               @Override
                                                 public Dimension getPreferredSize() {
                                                return new Dimension(700, 600);
                                             }
                                            }; 
                                           
                                          
                                           
                                           JOptionPane.showMessageDialog(null, scrollPane, doc ,JOptionPane.PLAIN_MESSAGE);
                                       

                                   }else{
                                      
                                            String string = new String(Files.readAllBytes(Paths.get(doc)), StandardCharsets.UTF_8);
                                            JTextArea jta =new JTextArea (string);
                                            JScrollPane scrollPane=  new JScrollPane(jta){
                                                @Override
                                                public Dimension getPreferredSize() {
                                                    return new Dimension(700, 600);
                                                }
                                            };
                                            
                                            
                                            
                                            JOptionPane.showMessageDialog(null, scrollPane, doc ,JOptionPane.PLAIN_MESSAGE);
                                        
                                   }
                                 } catch (IOException ex) {
                                            Logger.getLogger(JFrameResults.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                        
                                        
                                }
                                        
                                    

                                
				
				scrollPane.setVisible(true);

			}
		});
		
		table.setAutoResizeMode(JTable.WIDTH);
                table.setAutoCreateRowSorter(true);
                table.setAutoscrolls(true);
   
                scrollPane = new JScrollPane(table);

                
		
		panel1.add(label);
                panel2.add(scrollPane);
                panel3.add(exitBut);
                //Container cont = getContentPane();
                
                panel4.add(panel1, BorderLayout.NORTH);
                panel4.add(panel2, BorderLayout.AFTER_LAST_LINE);
                
                panel4.add(panel3, BorderLayout.PAGE_END);
             
                this.add(panel4);
                this.setVisible(true);


    }     

    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Come back")){
            this.dispose();
            JSearchMenu.main(null);
            
        }
        
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        
    }

}
