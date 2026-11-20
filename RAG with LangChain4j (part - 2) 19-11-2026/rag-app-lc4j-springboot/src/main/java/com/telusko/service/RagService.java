package com.telusko.service;

import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import com.telusko.assistant.Assistant;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import jakarta.annotation.PostConstruct;

@Service
public class RagService 
{
	
    private final EmbeddingStoreIngestor ingestor;
    
    private final Assistant assistant;
    public RagService(
            EmbeddingStoreIngestor ingestor,
            Assistant assistant) {

        this.ingestor = ingestor;
        this.assistant = assistant;
    }
    
    @PostConstruct
    public void seedKnowledgeBase() throws Exception
    {
    	// WHAT: Spring utility for finding resource files.
        // WHY: It helps us read files from the classpath.
        var resolver =
                new PathMatchingResourcePatternResolver();

        // Find all .txt files inside resources/docs.
        //
        // classpath:docs/*.txt
        //
        // Only matching text files are loaded.
        Resource[] files =
                resolver.getResources("classpath:docs/*.txt");

        // Count how many documents we process.
        int count = 0;

        // Process each document one by one.
        for (Resource file : files) {

            // WHAT: Read the file content as text.
            // WHY: Our embedding model needs text input.
            //
            // UTF-8 helps read normal text and
            // supported special characters correctly.
            String text = new String(
                    file.getInputStream().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            // WHAT: Convert Java String into
            // a LangChain4j Document.
            //
            // WHY: The ingestor works with Document objects.
            Document doc = Document.from(text);

            // WHAT: Add the original filename as metadata.
            //
            // WHY: Metadata helps identify where
            // a document came from.
            //
            // Example:
            // source = "java.txt"
            //
            // Metadata can be useful for showing
            // document sources or filtering later.
            doc.metadata().put(
                    "source",
                    file.getFilename()
            );

            // MOST IMPORTANT STEP: INGESTION
            //
            // This calls our configured ingestor.
            //
            // Internally:
            //
            // Document
            //    |
            //    v
            // Split into smaller chunks
            //    |
            //    v
            // Convert chunks into embeddings
            //    |
            //    v
            // Save embeddings and text in vector store
            //
            // After this, the document content
            // becomes searchable through RAG.
            ingestor.ingest(doc);

            count++;
        }

        // Print how many documents were processed.
        System.out.println(
                "[RAG] Ingested "
                        + count
                        + " document(s) into the vector store."
        );
    }

    public String ask(String question) {

        // AiServices handles the RAG flow
        // using our configured ContentRetriever.
        return assistant.answer(question);
    }


}
