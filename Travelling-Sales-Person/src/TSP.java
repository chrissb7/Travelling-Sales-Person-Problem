import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;



//PLEASE BE AWARE MAP.PNG IS USED IN THIS PROJECT. PLEASE ADD map.png TO SAME DIRECTORY

public class TSP {

    public static void main ( String [] args){


        GUI theGui = new GUI();


    }

    public static int plotPoint(double lat1, double long1, double lat2, double long2) {
        double latitude1 = lat1;
        double longitude1 = long1;
        double latitude2 = lat2;
        double longitude2 = long2;


        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLon = Math.toRadians(longitude2 - longitude1);


        latitude1 = Math.toRadians(latitude1);
        latitude2 = Math.toRadians(latitude2);

        double formPt1 = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) *
                Math.cos(latitude1) * Math.cos(latitude2);


        double radius = 6371;

        double formPt2 = 2 * Math.asin(Math.sqrt(formPt1));

        int result = (int) ((int) radius * formPt2);

        return result;

    }

    public static class GUI implements ActionListener{

        //Building of GUI elements with a series of nesting elements within one another
        //This includes placing text areas in panels and providing scroll functionality.

        JFrame frame;
        JButton button;
        JTextArea textArea;
        JScrollPane scrollPane;
        JScrollPane scrollResults;
        JTextArea results;
        JPanel imageHost=new JPanel();



        public GUI(){

            textArea = new JTextArea(5, 20);
            textArea.setBounds(50,70,300,300);
            scrollPane=new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            results = new JTextArea(5, 20);
            results.setBounds(25,500,470,300);
            scrollResults=new JScrollPane(results, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);


            button = new JButton("Calculate");
            button.addActionListener(this);
            button.setBounds(150,400,200,60);
            button.setFont(new Font("Calibri", Font.PLAIN,25));
            button.setForeground(new Color(250,250,250));
            button.setBackground(new Color(250,250,250));
            button.setBorder(BorderFactory.createEtchedBorder());


            ImageIcon m = new ImageIcon("map.png");
            JLabel image = new JLabel(m);
            image.setBounds(200,200,1000,1000);
            imageHost.setSize(new Dimension(900,900));
            imageHost.setBounds(540,10,900,900);
            imageHost.add(image);



            JPanel scroll = new JPanel();
            scroll.setSize(new Dimension(300,300));
            scroll.setBounds(25,40,470,300);
            scroll.add(scrollPane);
            scroll.setLayout(new BorderLayout());
            scroll.add(scrollPane, BorderLayout.CENTER);

            JPanel scrollResultsPane = new JPanel();
            scrollResultsPane.setSize(new Dimension(300,300));
            scrollResultsPane.setBounds(25,500,470,300);
            scrollResultsPane.add(scrollResults);
            scrollResultsPane.setLayout(new BorderLayout());
            scrollResultsPane.add(scrollResults, BorderLayout.CENTER);


            JPanel panel = new JPanel();
            panel.setSize(new Dimension(1000,1000));
            panel.setBounds(10,10,500,880);
            panel.setBackground(new Color(12, 11, 11));

            frame = new JFrame("TSP");
            frame.setSize(new Dimension(2000,2000));
            frame.setLayout(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(button);
            frame.add(scroll);
            frame.add(scrollResultsPane);
            frame.add(panel);
            frame.add(imageHost);
            frame.setVisible(true);

        }



        @Override
        public void actionPerformed(ActionEvent e) {
            //ArrayList instantiated to stor Location objects (see Location objects at end of code)
            ArrayList<Location> locations = new ArrayList<Location>();
            //Scanner used to get input from user on actionPerformed on Calculate button
            String Input= textArea.getText();
            //Apache Pizza location hardcoded in as 1st element
            locations.add(new Location(0, 0, 53.38197, -6.59274));
            Scanner input = new Scanner(Input);

            //hasNextLine gets input from GUI
            while(input.hasNextLine()){
                //Splitting up of the address is stored in the following variables
                String a=input.nextLine();
                String[] arrOfStr = a.split(",", 6);
                int order = Integer.parseInt(arrOfStr[0].trim());
                int minz = Integer.parseInt(arrOfStr[2].trim());
                double lat = Double.parseDouble(arrOfStr[3].trim());
                double longitude = Double.parseDouble(arrOfStr[4].trim());
                locations.add(new Location(order, minz, lat, longitude));

            }
            int Size = locations.size();
            //Size variable of arraylist used to store size of Arraylist and used in constructing orderRoute object-
            //2D array structure.

            orderRoute [][]matrix = new orderRoute[Size][Size];
            //Nested for loop adds new orderRoute object
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j <matrix[i].length; j++) {
                    //Location objects are compared against each other
                    //E.g. order/ 0 or 0 index Location object of ArrayList(Apache Pizza) is compared against all other Locations
                    // E.g. Location 0 compared against Location 1, Location 0 compared against Location 2...etc.
                    Location compare=locations.get(i);
                    Location compare2=locations.get(j);


                    int timeTaken =plotPoint(compare.latitude, compare.longitude, compare2.latitude,compare2.longitude);
                    //position in Matrix takes in orderRoute object which has references to the objects used to find distance between each other
                    //plotPoint method gets the distance which is represented as minutes in the object because 60km/h 1 km a minute
                    matrix[i][j]= new orderRoute(timeTaken, compare.order, compare, compare2);

                }
            }

            //solution stores the order numbers in string "0,1,3,4..."
            String solution="0,";
            //Rows means orders, start off on order/row 0 check nearest neighbor
            //Nearest neighbor is 1, then update row to 1 and search through order/row 1 to find nearest neighbor
            int rows=0;
            for (int i = 0; i < matrix.length; i++) {
                int orders=0; //Orders keeps track of order that is nearest neighbor
                int orderMins = 0;//orderMins keeps track of the distance/time to the nearest neighbor
                orderRoute result = matrix[rows][0];//This is used to change boolean visited variable to true
                for (int j = 0; j < matrix[rows].length; j++) {
                    try {           //orderRoute objects contain references to location objects whose . properties can be accessed as needed
                        int edgeCase1 = matrix[rows][j].location1.order; //Represents orderRoute[i][j].location1.order
                        int edgeCase2 = matrix[rows][j].location2.order;////Represents orderRoute[i][j].location2.order
                        boolean visited = matrix[rows][j].location2.visited; //If location 2 of a orderRoute[i][j].location.visited is checked

                        //If the visited boolean is checked as true then skip over iteration, thus avoiding going back to an order/row on previously
                        if (visited) {
                            continue;
                        }
                        //If the orderRoute object at matrix[i][j] have equal objects i.e. location1 and location2 objects point to the same Location object than skip
                        //This avoids considering where you're current position is as the shortest distance
                        if (edgeCase1==edgeCase2){
                            continue;
                        }

                        if (orderMins == 0 && edgeCase1 == edgeCase2) {
                            continue;
                        }

                        //Below variables ensure that the order and orderMins are updated when comparing

                        //orders are equal to 0 but the orderRoute object at matrix[i][j], location1 and location2 objects don't point to the same Location object
                        //update the orders and orderMins
                        if (orders == 0 && edgeCase1 != edgeCase2) {
                            orderMins = matrix[rows][j].minutes;
                            orders = matrix[rows][j].location2.order;
                        }

                        //orderMins is greater than 0 and orderMins are greater than the orderRoute object at matrix[i][j]
                        //Update the orderMins and orders with orderRoute object variables  at matrix[i][j]
                        if (orderMins >0 && orderMins > matrix[rows][j].minutes) {
                            orderMins = matrix[rows][j].minutes;
                            orders = matrix[rows][j].location2.order;
                        }
                        //else if the orderRoute object at matrix[i][j] is greater than current order mins than don't update
                        if (orderMins>0 && orderMins < matrix[rows][j].minutes) {
                            continue;
                        }
                    }
                    catch(ArrayIndexOutOfBoundsException exception) {
                        continue;
                    }



                }
                result.location1.visited=true; // variable orderRoute result = matrix[rows][0] is checked
                //Ensures object won't be factored in as a closest neighbor again
                if(orders==0){
                    break;
                }

                else{
                    solution+= Integer.toString(orders) +","; //solution adds orders to string
                    rows=orders; //The orders variable also represents the row to go to next e.g. 1 is the closest neighbor
                    //go to row 1 i.e. order 1 and search through this row
                }
            }
            //String is printed to the GUI
            String s = solution.substring(0, solution.length()-1);
            System.out.println(s);
            results.setText(s);
            input.close();
        }

    }


    public static class Location { //Represents an order, order number, minutes means angry minutes
        int order;
        int minutes;
        double latitude;
        double longitude;
        boolean visited; //If this is checked than do not use this location anymore

        Location(int orderNo, int mins, double lat, double longi){ //Constructor
            this.order=orderNo;
            this.minutes=mins;
            this.latitude=lat;
            this.longitude=longi;
        }

    }


    public static class orderRoute{ //contains 2 Location object references, and the minutes/distance between those 2 objects (orders)
        int minutes;
        int route;
        Location location1;
        Location location2;


        orderRoute(int mins, int r, Location one, Location two){ //Constructor
            this.route= r;
            this.minutes=mins;
            this.location1=one;
            this.location2=two;
        }
    }
}


