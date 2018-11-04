package testsJvn2;

import jvn.IOAnnotation;

public interface SentenceItf {
	@IOAnnotation(mode = "write")
	public void write(String text);
	@IOAnnotation(mode = "read")
	public String read();
}
