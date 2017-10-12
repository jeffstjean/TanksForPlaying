import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;

public class Controller implements KeyListener{

    private Game game;
    
public Controller(Game g){
    bind(KeyEvent.VK_W, Key.up);
    bind(KeyEvent.VK_A, Key.left);
    bind(KeyEvent.VK_S, Key.down);
    bind(KeyEvent.VK_D, Key.right);
    
    game = g;
}

@Override
public void keyPressed(KeyEvent e) {
    other[e.getExtendedKeyCode()] = true;
    keyBindings.get(e.getKeyCode()).isDown = true;
}

@Override
public void keyReleased(KeyEvent e) {
    other[e.getExtendedKeyCode()] = false;
    keyBindings.get(e.getKeyCode()).isDown = false;
}

public boolean isKeyBinded(int extendedKey){
    return keyBindings.containsKey(extendedKey);
}

@Override
public void keyTyped(KeyEvent e) {
}


public void bind(Integer keyCode, Key key){
    keyBindings.put(keyCode, key);
}

public void releaseAll(){
    for(Key key : keyBindings.values()){
        key.isDown = false;
    }
}

public HashMap<Integer, Key> keyBindings = new HashMap<Integer, Key>();
public static boolean other[] = new boolean[256];

}