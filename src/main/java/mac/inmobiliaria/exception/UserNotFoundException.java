package mac.inmobiliaria.exception;

public class UserNotFoundException extends IllegalArgumentException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Long id) {
		super(String.format("User with Id %d not found", id));
	}

}
