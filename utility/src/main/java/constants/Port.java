package main.java.constants;

public class Port {

	/**
	 * Port on which the login service is listen.
	 */
	public static final int LOGIN_SERVICE = 25712;

	/**
	 * Port on which the register service is listen.
	 */
	public static final int REGISTER_SERVICE = 25713;

	/**
	 * Port on which the verify service is listen.
	 */
	public static final int VERIFY_SERVICE = 25714;

	/**
	 * Port on which the reset service is listen.
	 */
	public static final int RESET_SERVICE = 25715;

	/**
	 * Port on which the rendezvous service is listen.<br>
	 * Be careful the rendezvous service is not reachable before successful
	 * login.<br>
	 * This constant is normally not used.
	 */
	public static final int RENDEZVOUS_SERVICE = LOGIN_SERVICE;
}
