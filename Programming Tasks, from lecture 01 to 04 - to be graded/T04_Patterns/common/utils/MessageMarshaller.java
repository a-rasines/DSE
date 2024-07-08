package common.utils;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MessageMarshaller {
	public static List<String> demarshall(String input) {
		List<String> output = new LinkedList<>();
		String[] s = input.split(";");
		String actual = "";
		boolean empty = false;
		for(String a : s)
			if(a.equals(""))
				if(empty)
					empty = false;
				else {
					empty = true;
					actual += ";";
				}
			else 
				if(actual != "" && !empty) {
					output.add(actual);
					actual = a;
				} else {
					empty = false;
					actual += a;
				}
		output.add(actual);
		return output;
	}
	public static String marshall(Collection<String> input) {
		String output = "";
		for(String s : input)
			output += s.replace(";", ";;") + ";";
		return output.substring(0, output.length()-1); //remove last ";"
	}
}
