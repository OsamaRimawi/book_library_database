package login;

public class User {

	private int id;
	private String firstName;
	private String lastName;
	private String gender;
	private String birthDate;
	private String finantialPalance;
	private double fees;

	public User() {

	}

	public User(int id, String firstName, String lastName, String gender, String birthDate, String finantialPalance,
			double fees) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = gender;
		this.birthDate = birthDate;
		this.finantialPalance = finantialPalance;
		this.fees = fees;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getFinantialPalance() {
		return finantialPalance;
	}

	public void setFinantialPalance(String finantialPalance) {
		this.finantialPalance = finantialPalance;
	}

	public double getFees() {
		return fees;
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", gender=" + gender
				+ ", birthDate=" + birthDate + ", finantialPalance=" + finantialPalance + ", fees=" + fees + "]";
	}

}
