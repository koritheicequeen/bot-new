package mybotproject;

import java.awt.GraphicsEnvironment;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String fonts[] = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
 
        for( String font : fonts )
            System.out.println( font );
	}

}
