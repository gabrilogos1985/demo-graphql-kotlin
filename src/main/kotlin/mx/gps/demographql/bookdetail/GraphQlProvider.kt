package mx.gps.demographql.bookdetail

import com.google.common.io.Resources
import graphql.schema.GraphQLSchema
import graphql.GraphQL
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.io.IOException
import kotlin.text.Charsets
import javax.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaGenerator
import graphql.schema.idl.SchemaParser
import graphql.schema.idl.TypeDefinitionRegistry
import graphql.schema.idl.TypeRuntimeWiring.newTypeWiring


@Component
class GraphQLProvider {

    private var graphQL: GraphQL? = null
    @Autowired
    var graphQLDataFetchers: GraphQLDataFetchers? = null

    @Bean
    fun graphQL(): GraphQL? {
        return graphQL
    }

    @PostConstruct
    @Throws(IOException::class)
    fun init() {
        val url = Resources.getResource("schema.graphqls")
        val sdl = Resources.toString(url, Charsets.UTF_8)
        val graphQLSchema = buildSchema(sdl)
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build()
    }

    private fun buildSchema(sdl: String): GraphQLSchema {
        val typeRegistry = SchemaParser().parse(sdl)
        val runtimeWiring = buildWiring()
        val schemaGenerator = SchemaGenerator()
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring)
    }

    private fun buildWiring(): RuntimeWiring {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers?.getBookByIdDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers?.getAuthorDataFetcher()))
                .build()
    }
}