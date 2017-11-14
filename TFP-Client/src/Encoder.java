
import java.nio.ByteBuffer;


public class Encoder {
    private static ByteBuffer bb;
    private static long maxMillis = 0;
    
    public static byte[] createBytes() {
        byte[] allBytes = new byte[256];
        allBytes[0] = 1; // says its an in game byte
        
        //sets the up byte to 1 or 0 
        //0 is pressed 1 is not pressed
        if (Key.up.isDown) {
            allBytes[1] = 0;
        } else {
            allBytes[1] = 1;
        }

        
        //sets the down byte to 1 or 0 
        //0 is pressed 1 is not pressed
        if (Key.down.isDown) {
            allBytes[2] = 0;
        } else {
            allBytes[2] = 1;
        }

        
        //sets the left byte to 1 or 0 
        //0 is pressed 1 is not pressed
        if (Key.left.isDown) {
            allBytes[3] = 0;
        } else {
            allBytes[3] = 1;
        }

        
        //sets the right byte to 1 or 0 
        //0 is pressed 1 is not pressed
        if (Key.right.isDown) {
            allBytes[4] = 0;
        } else {
            allBytes[4] = 1;
        }

        
        //the x encoder start
        bb = ByteBuffer.allocate(4); // byte buffer becomes 4 bytes long
        bb.putInt(Game.getMouseX()); // puts the int in the buffer
        byte[] temp = bb.array(); // sets the array temp to equal the buffer
        for (int i = 0; i < temp.length; i++) {
            allBytes[i + 5] = temp[i]; // sets the selected allBytes values to the correct bytes
        }
        // the x encoder end
        
        
        // the y encoder start
        bb = ByteBuffer.allocate(4);
        bb.putInt(Game.getMouseY());
        temp = bb.array();
        for (int i = 0; i < temp.length; i++) {
            allBytes[i + 9] = temp[i];
        }
        // y encoder end

        if (Key.shoot.isDown) {
            allBytes[13] = 0;
        } else {
            allBytes[13] = 1;
        }

        bb = ByteBuffer.allocate(8);
        bb.putLong(System.currentTimeMillis());
        temp = bb.array();
        for (int i = 0; i < temp.length; i++) {
            allBytes[i + 15] = temp[i];
        }
        allBytes[24] = (byte) Game.PLAYERNUMBER;
        return allBytes;
    }
    
    
    public static void decodeBytes(byte[] bmain, Tank[] tank, Turret[] turret) {
        System.out.println("recieved");
        
        //decodes the time that this packet was originally sent
        byte[] temp = new byte[8]; // inits the temp byte[] as an 8 byte array
        if (bmain[0] == 1) { // only runs this if the packet is a real packet not a blank packet
            for (int i = 0; i < 8; i++) {
                temp[i] = bmain[i + 240]; // sets the byes in temp to be the 8 bytes i want decoded
            }

            bb = ByteBuffer.wrap(temp);//gives he buffer temp[] to read from 
            long tempL = bb.getLong(); // reads the long value to a tempL variable
            
            if (/*maxMillis < tempL*/ true ) {//only uses the packet if it is a new packet //this is commented out for debug purposes
                maxMillis = tempL; // sets the most recent packet index to the current packts index number

                for (int j = 0; j < Game.NUM_PLAYERS; j++) { // decodes the basic player info the number of times as there are players
                    //x decoder start
                    temp = new byte[4]; // re-sets the length of temp to 4
                    for (int i = 0; i < 4; i++) {
                        temp[i] = bmain[i + 1 + (20 * j)]; // sets temp to be the 4 bytes that represent the x location of tank[j]
                    }
                    bb = ByteBuffer.wrap(temp); // gives the buffer the temp
                    tank[j].setX(bb.getInt()); // sets tank[j]'s location
                    // x decoder end
                    
                    //y decoder start, please see x decoder for reference
                    temp = new byte[4];
                    for (int i = 0; i < 4; i++) {
                        temp[i] = bmain[i + 5 + (20 * j)];
                    }
                    bb = ByteBuffer.wrap(temp);
                    tank[j].setY(bb.getInt());
                    //y decoder end
                    
                    //rotation encoder start, please see x encoder for reference
                    temp = new byte[8]; 
                    for (int i = 0; i < 8; i++) {
                        temp[i] = bmain[i + 10 + (20 * j)];
                    }
                    bb = ByteBuffer.wrap(temp);
                    turret[j].setRotate(bb.getDouble()); // sets the turrets pointing to the pointing specified by the server
                    //rotation decoder end
                    
                    
                    // shooting decoder start
                    if (bmain[19 + (20 * j)] == 0) {
                        turret[j].shoot(); // if the shooting byte = 0 it will shoot
                    }
                    //shooting encoder end
                    
                    
                    tank[j].setPointing(bmain[20 + (20 * j)]); // sets the pointing enum to the numerical value of where the server is telling the client
                    
                    
                   
                }

            } else {
                System.out.println("got an old packet"); 
            }
        }

    }
    
    
    
}
