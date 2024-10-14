package defaultpackage;

public class Books {
	private String name, author, ISBN, genre, price;
	
	public Books() {
		super();
	}
	public Books(String name, String author, String ISBN, String genre, String price) {
		super();
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
	public String getPrice(){
		return price;
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
	public void setPrice(String price) {
		this.price = price;
	}
	
}