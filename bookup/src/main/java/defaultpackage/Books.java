package defaultpackage;

public class Books {
	private int ISBN; //key attribute
	private String genre, publisher, author, name;
	
	public Books() {
		super();
	}
	
	//default
	public Books(int ISBN, String genre, String publisher, String author, String name) {
		super();
		this.ISBN = ISBN;
		this.genre = genre;
		this.publisher = publisher;
		this.author = author;
		this.name = name;
	}

	public int getISBN() {
		return ISBN;
	}

	public void setISBN(int iSBN) {
		ISBN = iSBN;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
