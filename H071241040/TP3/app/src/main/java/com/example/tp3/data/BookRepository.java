package com.example.tp3.data;

import com.example.tp3.R;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public final class BookRepository {
    private static final List<Book> BOOKS = new ArrayList<>();
    private static int nextId = 1;

    static {
        seedBooks();
    }

    private BookRepository() {
    }

    public static List<Book> getAllBooks() {
        List<Book> sorted = new ArrayList<>(BOOKS);
        sorted.sort(
                Comparator.comparingInt(Book::getYear).reversed()
                        .thenComparing(Comparator.comparingInt(Book::getId).reversed())
        );
        return sorted;
    }

    public static List<String> getGenres() {
        Set<String> genres = new HashSet<>();
        for (Book book : BOOKS) {
            genres.add(book.getGenre());
        }
        List<String> sortedGenres = new ArrayList<>(genres);
        sortedGenres.sort(String::compareToIgnoreCase);
        return sortedGenres;
    }

    public static List<Book> getFavoriteBooks() {
        List<Book> favorites = new ArrayList<>();
        for (Book book : getAllBooks()) {
            if (book.isLiked()) {
                favorites.add(book);
            }
        }
        return favorites;
    }

    public static List<Book> searchBooks(String query, String genre) {
        String lowerQuery = (query == null) ? "" : query.toLowerCase(Locale.ROOT).trim();
        boolean filterByGenre = genre != null && !genre.isEmpty();

        List<Book> results = new ArrayList<>();
        for (Book book : getAllBooks()) {
            boolean matchesQuery = lowerQuery.isEmpty() || 
                    book.getTitle().toLowerCase(Locale.ROOT).contains(lowerQuery);
            boolean matchesGenre = !filterByGenre || book.getGenre().equalsIgnoreCase(genre);

            if (matchesQuery && matchesGenre) {
                results.add(book);
            }
        }
        return results;
    }

    public static List<Book> searchBooks(String query) {
        return searchBooks(query, null);
    }

    public static Book getBookById(int id) {
        for (Book book : BOOKS) {
            if (book.getId() == id) {
                return book;
            }
        }
        return null;
    }

    public static void toggleLike(int id) {
        Book book = getBookById(id);
        if (book != null) {
            book.setLiked(!book.isLiked());
        }
    }

    public static void addBook(
            String title,
            String author,
            int year,
            String blurb,
            String coverUri,
            String genre,
            float rating,
            String review
    ) {
        BOOKS.add(new Book(
                nextId++,
                title,
                author,
                year,
                blurb,
                null,
                coverUri,
                genre,
                rating,
                review,
                false
        ));
    }

    private static void seedBooks() {
        addBookResource(
                "Atomic Habits",
                "James Clear",
                2018,
                "Panduan membangun kebiasaan kecil yang konsisten untuk perubahan besar.",
                "Self Improvement",
                4.8f,
                "Praktis, mudah dipahami, dan aplikatif."
        );
        addBookResource(
                "Project Hail Mary",
                "Andy Weir",
                2021,
                "Kisah sains fiksi tentang misi penyelamatan bumi yang menegangkan.",
                "Sci-Fi",
                4.7f,
                "Seru, cerdas, dan penuh kejutan."
        );
        addBookResource(
                "The Midnight Library",
                "Matt Haig",
                2020,
                "Perjalanan antara kemungkinan hidup yang berbeda dari satu keputusan.",
                "Fiction",
                4.3f,
                "Emosional dan reflektif."
        );
        addBookResource(
                "Deep Work",
                "Cal Newport",
                2016,
                "Cara bekerja fokus di era penuh distraksi digital.",
                "Productivity",
                4.6f,
                "Membantu meningkatkan kualitas kerja."
        );
        addBookResource(
                "Educated",
                "Tara Westover",
                2018,
                "Memoar tentang perjuangan pendidikan dan identitas diri.",
                "Biography",
                4.5f,
                "Kuat, jujur, dan menginspirasi."
        );
        addBookResource(
                "The Psychology of Money",
                "Morgan Housel",
                2020,
                "Pelajaran keuangan yang berfokus pada perilaku manusia.",
                "Finance",
                4.7f,
                "Relevan untuk keputusan finansial sehari-hari."
        );
        addBookResource(
                "Sapiens",
                "Yuval Noah Harari",
                2014,
                "Sejarah singkat evolusi manusia dari masa ke masa.",
                "History",
                4.6f,
                "Wawasan luas dan memancing diskusi."
        );
        addBookResource(
                "Dune",
                "Frank Herbert",
                1965,
                "Epik politik dan ekologi di planet gurun Arrakis.",
                "Sci-Fi",
                4.5f,
                "World-building yang sangat kuat."
        );
        addBookResource(
                "The Silent Patient",
                "Alex Michaelides",
                2019,
                "Thriller psikologis tentang misteri pembunuhan yang membisu.",
                "Thriller",
                4.2f,
                "Plot twist menarik dan cepat dibaca."
        );
        addBookResource(
                "Norwegian Wood",
                "Haruki Murakami",
                1987,
                "Roman coming-of-age dengan nuansa melankolis.",
                "Literary Fiction",
                4.1f,
                "Puitis dan penuh emosi."
        );
        addBookResource(
                "Clean Code",
                "Robert C. Martin",
                2008,
                "Prinsip menulis kode yang rapi dan mudah dipelihara.",
                "Programming",
                4.7f,
                "Wajib untuk software engineer."
        );
        addBookResource(
                "The Pragmatic Programmer",
                "Andrew Hunt & David Thomas",
                1999,
                "Best practice membangun karier dan produk software berkualitas.",
                "Programming",
                4.8f,
                "Klasik yang tetap relevan."
        );
        addBookResource(
                "Thinking, Fast and Slow",
                "Daniel Kahneman",
                2011,
                "Dua sistem berpikir yang membentuk keputusan manusia.",
                "Psychology",
                4.4f,
                "Membuka perspektif pengambilan keputusan."
        );
        addBookResource(
                "A Man Called Ove",
                "Fredrik Backman",
                2012,
                "Cerita hangat tentang pria pemarah yang menemukan makna hidup.",
                "Fiction",
                4.5f,
                "Humor dan haru seimbang."
        );
        addBookResource(
                "The Song of Achilles",
                "Madeline Miller",
                2011,
                "Retelling mitologi Yunani dengan fokus hubungan Achilles dan Patroclus.",
                "Fantasy",
                4.6f,
                "Indah, tragis, dan berkesan."
        );
    }

    private static void addBookResource(
            String title,
            String author,
            int year,
            String blurb,
            String genre,
            float rating,
            String review
    ) {
        int[] covers = {
                R.drawable.bg_cover_blue,
                R.drawable.bg_cover_green,
                R.drawable.bg_cover_orange,
                R.drawable.bg_cover_purple,
                R.drawable.bg_cover_red
        };

        BOOKS.add(new Book(
                nextId++,
                title,
                author,
                year,
                blurb,
                covers[(nextId - 2) % covers.length],
                null,
                genre,
                rating,
                review,
                false
        ));
    }
}
