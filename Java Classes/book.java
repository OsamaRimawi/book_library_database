package login;

public class book {
	private String book_name;
	private int number_of_copy;
	private double price;
	private double borrowing_pricePerDay;
	private String book_description;
	private String date_of_publish;
	private int publisher_id;	
	
	public book() {
	}
	
	public book(String book_name,int number_of_copy ,double price ,double borrowing_pricePerDay,
			String book_description ,String date_of_publish ,int publisher_id) {
		super();
		this.book_name = book_name;
		this.number_of_copy = number_of_copy;
		this.price = price;
		this.borrowing_pricePerDay = borrowing_pricePerDay;
		this.book_description = book_description;
		this.date_of_publish = date_of_publish;
		this.publisher_id = publisher_id;
		}

	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	
	public int getNumber_of_copy() {
		return number_of_copy;
	}
	public void setNumber_of_copy(int number_of_copy) {
		this.number_of_copy = number_of_copy;
	}
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}

	public double getBorrowing_pricePerDay() {
		return borrowing_pricePerDay;
	}
	public void setBorrowing_pricePerDay(double borrowing_pricePerDay) {
		this.borrowing_pricePerDay = borrowing_pricePerDay;
	}
	
	public String getBook_description() {
		return book_description;
	}
	public void setBook_description(String book_description) {
		this.book_description = book_description;
	}

	public String getDate_of_publish() {
		return date_of_publish;
	}
	public void setDate_of_publish(String date_of_publish) {
		this.date_of_publish = date_of_publish;
	}
	
	public int getPublisher_id() {
		return publisher_id;
	}
	public void setPublisher_id(int publisher_id) {
		this.publisher_id = publisher_id;
	}
	
	@Override
	public String toString() {
		return "book [book_name=" + book_name + ", number_of_copy=" + number_of_copy + ", price=" + price + ", borrowing_pricePerDay=" 
	+ borrowing_pricePerDay + ", book_description=" + book_description + ", date_of_publish=" + date_of_publish
	+ ", publisher_id=" + publisher_id  + "]";
	}

}
