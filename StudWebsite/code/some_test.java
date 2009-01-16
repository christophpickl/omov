public class HelloWorld implements IHelloWorld {
	
	/**
	 * Says hello to given name.
	 * @param name a person's name, may be null
	 */
	public void sayHello(final String name) {
		final StringBuilder message = new StringBuilder("Hello ");
		
		if(name == null || name.trim().length() == 0) {
			message.append("World");
		} else {
			message.append(name);
		}
		message.append("!");
		
		System.out.println(message);
	}
	
}