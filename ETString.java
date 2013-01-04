import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class ETString {
	enum Type {
		Default, Percent, Double, KeepOn
	}
	
	/**
	 * This method is for using placedholders like <code>[NSString stringWithFormat:@"Value 1: %@ Value 2: %.2f"]</code> in Objective-C.<br>
	 * There are 2 different placeholders:<br>
	 * <ul>
	 * <li>%@ - Default (uses <code>toString()</code>)</li>
	 * <li>%f - Float/Double (you can use %.Xf like in Objective-C to format the Float/Double to X characters after the comma)</li>
	 * </ul>
	 * <br> 
	 * Wrong % gets replaced by #. If you still want to use a % in a String use %% instead.
	 * @param string String with placeholder(s)
	 * @param objects Objects to replace the placeholders
	 * @return String with replaced placeholders
	 */
	public static String withFormat(String string, Object...objects) {
		if (!string.endsWith("%%") && string.endsWith("%"))
			string = string.substring(0, string.length()-1)+"#";
		Map<String,Type> placeholders = new HashMap<String, Type>();
		placeholders.put("@", Type.Default);
		placeholders.put("%", Type.Percent);
		placeholders.put("f", Type.Double);
		placeholders.put(".", Type.KeepOn);
		int position = 0;
		int count = 0;
		while (string.substring(position).indexOf("%") >= 0) {
			position = string.indexOf(string.substring(position))+string.substring(position).indexOf("%");
			String nextPlaceholder = string.substring(position+1,position+2);
			if (placeholders.get(nextPlaceholder) == null || (count >= objects.length && placeholders.get(nextPlaceholder) != Type.Percent)) {
				string = string.substring(0,position)+"#"+string.substring(position+1);
			} else {
				Type type = placeholders.get(nextPlaceholder);
				switch (type) {
				case Default:
					string = string.substring(0,position)+objects[count].toString()+string.substring(position+2);
					break;
				case Percent:
					string = string.substring(0,position)+string.substring(position+1);
					position++;
					continue;
				case Double:
					Double floatOrDouble =(Double) objects[count];
					string = string.substring(0,position)+floatOrDouble.toString()+string.substring(position+2);
					break;
				case KeepOn:
					int indexF = string.substring(position+2).indexOf("f");
					if (indexF < 0) {
						string = string.substring(0,position)+"#"+string.substring(position+1);
						continue;
					}
					String gap = string.substring(position+2,position+2+indexF);
					Boolean isNaN = false;
					for (String character : gap.split("")) {
						if (!"1234567890".contains(character)) {
							isNaN = true;
							break;
						}
					}
					if (isNaN) {
						string = string.substring(0,position)+"#"+string.substring(position+1);
					} else {
						String zeroes = "";
						for (int i = 0; i < Integer.parseInt(gap); i++) {
							zeroes += "0";
						}
						NumberFormat nf = new DecimalFormat("0."+zeroes);
						string = string.substring(0,position)+nf.format(objects[count])+string.substring(position+3+gap.length());
					}
					break;
				default:
					break;
				}
				count++;
			}
		}
		return string;
	}
}