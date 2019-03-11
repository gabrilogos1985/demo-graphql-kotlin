package mx.gps.demographql.bookdetail

import com.google.common.collect.ImmutableMap
import graphql.schema.DataFetcher
import graphql.schema.DataFetchingEnvironment
import org.springframework.stereotype.Component
import java.util.*


@Component
class GraphQLDataFetchers {

    fun getBookByIdDataFetcher(): DataFetcher<Any> {
        return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
            val bookId: String = dataFetchingEnvironment.getArgument("id")
            books
                    .stream()
                    .filter { book -> book["id"].equals(bookId) }
                    .findFirst()
                    .orElse(null)
        }
    }

    fun getAuthorDataFetcher(): DataFetcher<Map<String, String>> {
        return DataFetcher { dataFetchingEnvironment: DataFetchingEnvironment ->
            val book: Map<String, String> = dataFetchingEnvironment.getSource()
            val authorId = book.get("authorId")
            authors
                    .stream()
                    .filter { author -> author["id"].equals(authorId) }
                    .findFirst()
                    .orElse(null)
        }
    }
    companion object {

        private val books: List<ImmutableMap<String, String>> = Arrays.asList(
                ImmutableMap.of("id", "book-1",
                        "name", "Harry Potter and the Philosopher's Stone",
                        "pageCount", "223",
                        "authorId", "author-1"),
                ImmutableMap.of("id", "book-2",
                        "name", "Moby Dick",
                        "pageCount", "635",
                        "authorId", "author-2"),
                ImmutableMap.of("id", "book-3",
                        "name", "Interview with the vampire",
                        "pageCount", "371",
                        "authorId", "author-3")
        )

        private val authors = Arrays.asList(
                ImmutableMap.of("id", "author-1",
                        "firstName", "Joanne",
                        "lastName", "Rowling"),
                ImmutableMap.of("id", "author-2",
                        "firstName", "Herman",
                        "lastName", "Melville"),
                ImmutableMap.of("id", "author-3",
                        "firstName", "Anne",
                        "lastName", "Rice")
        )
    }
}