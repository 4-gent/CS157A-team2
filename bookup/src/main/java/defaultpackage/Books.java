package defaultpackage;

public class Books {
	private String name, author, ISBN, genre;
	private float price;
	private int book_id;
	
	public Books() {
		super();
	}
	public Books(int book_id, String name, String author, String ISBN, String genre, float price) {
		super();
		this.book_id = book_id;
		this.name = name;
		this.author = author;
		this.ISBN = ISBN;
		this.genre = genre;
		this.price = price;
	}
	
	// Getters
	public String getName(){
		return name;
	}
	public String getAuthor(){
		return author;
	}
	public String getISBN(){
		return ISBN;
	}
	public String getGenre(){
		return genre;
	}
	public float getPrice(){
		return price;
	}
	public int getBook_ID(){
		return book_id;
	}
	
	//Setters
	public void setName(String name) {
		this.name = name;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public void setBook_ID(int book_id) {
		this.book_id = book_id;
	}
	
}