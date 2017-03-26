# TreasureHuntBLE

The main purpose of this project is to use Bluetooth beacons for localization for indoor 
usage as it is not a good idea to use GPS indoors due to the wall preventing GPS signals.


My task was to implement a treasure hunt gamed based on the location of the user inside a building using an android application.
A signal strength analysis was performed to test the amount of signal power decrease due to wall penetration to try to take 
a range to decide whether I am inside a room or not. An android application was developed to read signals constantly from 
the Bluetooth beacons keeping the beacon’s MAC addresses in a cloud database to be able to reach it at any time. The application was 
fully developed and the beacons were centralized inside every room to be able to locate it easily everywhere inside the room. 

NB> The Application will not work as the MAC addresses of the beacons must be the same om the cloud.

