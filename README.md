# Travelling-Sales-Person-Problem

This project was based on the Travelling Sales Person problem. An algorithm is developed in order to find out the most efficient route a sales person can take, given a number of locations. In the case of this algorithm a list of 100 orders is inputed (please look at accompaying 100Orders.txt file, copy and paste all into the console). 

Breaking up the first order which is numbered 0 (see 100Orders.txt file):

0,3 Mill St Maynooth,0,53.38197,-6.59274: 0 is the starting off location, 3 Mill St Maynooth is the address of the starting location and then the longitude of 0,53.38197 and latitude of -6.59274 is given.

From this starting point the next best location is calculated among the 100 orders. If order number 3 is the shortest distance from the starting point that is where the sales person will go. Than calculating from the location at order 3 the next best location is figured out among the remaining 99 locations. And so and so forth, until there are no locations left.

A simple UI was developed wherein upon running the program the data (orders) below can be copy and pasted in at the top white input box, and then the calculate button can be pressed with the results of the most efficient routes to take appearing in the display white box below the calculate button.

Please ensure the data is copy and pasted as is presented in the 100Orders.txt file

