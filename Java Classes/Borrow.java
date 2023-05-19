package login;

	public class Borrow {
		private String book_name;
		private int publisher_id;	
		private String date_of_borrow;
		private String end_date_of_borrow;
		private String return_date;
		private int lating_days;
		
		public Borrow() {
		}
		
		public Borrow(String book_name,int publisher_id ,String date_of_borrow  ,String end_date_of_borrow ,
				String return_date ,int lating_days ) {
			super();
			this.book_name = book_name;
			this.publisher_id = publisher_id;
			this.setDate_of_borrow(date_of_borrow);
			this.setEnd_date_of_borrow(end_date_of_borrow);
			this.setReturn_date(return_date);
			this.setLating_days(lating_days);
			}

		public String getBook_name() {
			return book_name;
		}
		public void setBook_name(String book_name) {
			this.book_name = book_name;
		}
		
		public int getPublisher_id() {
			return publisher_id;
		}
		public void setPublisher_id(int publisher_id) {
			this.publisher_id = publisher_id;
		}
		

		public String getDate_of_borrow() {
			return date_of_borrow;
		}

		public void setDate_of_borrow(String date_of_borrow) {
			this.date_of_borrow = date_of_borrow;
		}

		public String getEnd_date_of_borrow() {
			return end_date_of_borrow;
		}

		public void setEnd_date_of_borrow(String end_date_of_borrow) {
			this.end_date_of_borrow = end_date_of_borrow;
		}

		public String getReturn_date() {
			return return_date;
		}

		public void setReturn_date(String return_date) {
			this.return_date = return_date;
		}

		public int getLating_days() {
			return lating_days;
		}

		public void setLating_days(int lating_days) {
			this.lating_days = lating_days;
		}
		
		@Override
		public String toString() {
			return "Borrow [book_name=" + book_name + ", publisher_id=" + publisher_id + ", date_of_borrow=" + date_of_borrow + 
					", end_date_of_borrow=" + end_date_of_borrow + ", return_date=" + return_date + ", lating_days=" + lating_days
					+ "]";
		}

	}