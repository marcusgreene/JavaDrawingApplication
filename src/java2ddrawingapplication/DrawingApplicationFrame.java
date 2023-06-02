/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java2ddrawingapplication;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JSpinner;

/**
 *
 * @author acv
 */
public class DrawingApplicationFrame extends JFrame
{   
    //JFrame frame = new JFrame();

    // Create the panels for the top of the application. One panel for each
    // line and one to contain both of those panels.
    private JPanel firstLine = new JPanel();
    private JPanel secondLine = new JPanel();
    private JPanel topPanel = new JPanel();
    

    // create the widgets for the firstLine Panel.
    private JLabel firstLabel = new JLabel("First line");
    private String[] shapes = {"Line", "Oval", "Rectangle"};
    private JComboBox combo = new JComboBox<>(shapes);
    private JButton button1 = new JButton("Color 1");
    private JButton button2 = new JButton("Color 2");
    private JButton undoButton = new JButton("Undo");
    private JButton clearButton = new JButton("Clear");
    
    
    
    //create the widgets for the secondLine Panel.
    private JLabel secondLabel = new JLabel("second line");
    private JCheckBox checkbox1 = new JCheckBox("Filled");
    private JCheckBox checkbox2 = new JCheckBox("Use Gradiant");
    private JCheckBox checkbox3 = new JCheckBox("Dashed");
    private JLabel widthLabel = new JLabel("Line Width:");
    private JSpinner spinnerLabel = new JSpinner();
    private JLabel dashLabel = new JLabel("Dash Length:");
    private JSpinner spinnerLabel2 = new JSpinner();

    // Variables for drawPanel.

    //check this
    private DrawPanel drawPanel = new DrawPanel();
    private ArrayList<MyShapes> shapesArray = new ArrayList();
    private Color color1 = Color.WHITE;
    private Color color2 = Color.WHITE;
    private Paint paint = Color.BLACK;
    
    //check these
    private MyShapes currentShape;
    Float width = Float.parseFloat("10");
    Stroke stroke = new BasicStroke(width);


    // add status label
    private JLabel statusLabel = new JLabel("(Coordinates Here)");
  
    // Constructor for DrawingApplicationFrame
    public DrawingApplicationFrame() {

        // add widgets to panels
        firstLine.add(firstLabel);
        firstLine.add(combo);
        firstLine.add(button1);
        firstLine.add(button2);
        firstLine.add(undoButton);
        firstLine.add(clearButton);

        // firstLine widgets

        // secondLine widgets
        secondLine.add(secondLabel);
        secondLine.add(checkbox1);
        secondLine.add(checkbox2);
        secondLine.add(checkbox3);
        secondLine.add(widthLabel);
        secondLine.add(spinnerLabel);
        secondLine.add(dashLabel);
        secondLine.add(spinnerLabel2);

        // add top panel of two panels
        topPanel.setLayout(new GridLayout(2,1));
        topPanel.add(firstLine);
        topPanel.add(secondLine);

        // add topPanel to North, drawPanel to Center, and statusLabel to South
        add(topPanel, BorderLayout.NORTH);
        add(drawPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);
        //add listeners and event handlers

        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                color1 = JColorChooser.showDialog(null, "Choose a color", color1);
                if (color1==null) {
                    color1 = Color.BLACK;
    
                }
            }
        });
    
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                color2 = JColorChooser.showDialog(null, "Choose a color", color2);
                if (color2 == null) {
                    color2 = Color.BLACK;
                }
            }
        });

        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(shapesArray.size() > 0) {
                    shapesArray.remove(shapesArray.size()-1);
                    repaint();
                }
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) { 
                shapesArray.clear();
                repaint();
            }
        });
    }


    // Create event handlers, if needed
    

    // Create a private inner class for the DrawPanel.
    private class DrawPanel extends JPanel
    {

        public DrawPanel()
        {
            MouseHandler mouseHandler = new MouseHandler();
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        public void paintComponent(Graphics g)
        {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            for (int i=0; i < shapesArray.size(); i++) {
                MyShapes s = shapesArray.get(i);
                s.draw(g2d);
            }
        }


        private class MouseHandler extends MouseAdapter implements MouseMotionListener
        {

            public void mousePressed(MouseEvent event)
            {
                int xStart = event.getX();
                int yStart = event.getY();
                Point point = new Point(xStart, yStart);
                String shape = combo.getSelectedItem().toString();
                Boolean filled = checkbox1.isSelected();
                String spinner1Value = spinnerLabel.getValue() + "";
                width = Float.parseFloat(spinner1Value);
                float[] dash = {width};

                if(checkbox2.isSelected()) {
                    paint = new GradientPaint(0, 0, color1, 50, 50, color2, true);  

                }
                else {
                    paint = color1;
                }

                if(checkbox3.isSelected()) {
                    stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10, dash, 0);
                }
                else {
                    stroke = new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
                }

                if (shape.equals("Rectangle")) {
                    currentShape = new MyRectangle(point, point, paint, stroke, filled);
                }
                else if (shape.equals("Oval")) {
                    currentShape = new MyOval(point, point, paint, stroke, filled);
                }
                else {
                    currentShape = new MyLine(point, point, paint, stroke);
                }
                
                shapesArray.add(currentShape);
                repaint();

            }

            public void mouseReleased(MouseEvent event)
            {
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event)
            {
                int finalX = event.getX();
                int finalY = event.getY();
                Point finalPoint = new Point(finalX, finalY);
                shapesArray.get(shapesArray.size()-1).setEndPoint(finalPoint);
                repaint();


            }

            @Override
            public void mouseMoved(MouseEvent event)
            {
                statusLabel.setText("(" + event.getX() + "," + event.getY() + ")");
            }
        }

    }
}
