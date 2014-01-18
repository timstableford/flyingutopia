package flyingutopia.engine;

import java.io.FileNotFoundException;

import argo.format.JsonFormatter;
import argo.format.PrettyJsonFormatter;
import argo.saj.InvalidSyntaxException;

public class Engine {

	public static void main(String[] args) throws FileNotFoundException, InvalidSyntaxException {
		Resources res = new Resources();
		res.parseJson();
		res.loadImages();
		
	}

}
