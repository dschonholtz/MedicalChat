-- Create the papers table to store metadata
-- drop the papers and paper_vectors tables if they exist
DROP TABLE IF EXISTS papers CASCADE;
DROP TABLE IF EXISTS paper_vectors CASCADE;

CREATE TABLE IF NOT EXISTS papers (
                                      id SERIAL PRIMARY KEY,
                                      doi TEXT,
                                      title TEXT,
                                      authors TEXT,
                                      category TEXT,
                                      abstract TEXT,
                                      author_corresponding TEXT,
                                      author_corresponding_institution TEXT,
                                      date DATE,
                                      version TEXT,
                                      type TEXT,
                                      license TEXT,
                                      jatsxml TEXT,
                                      published TEXT,
                                      server TEXT
);

-- Create the paper_vectors table to store vectors for title and abstract segments
CREATE TABLE IF NOT EXISTS paper_vectors (
                                             id SERIAL PRIMARY KEY,
                                             paper_id INTEGER REFERENCES papers(id) ON DELETE CASCADE,
                                             title_vector VECTOR(384),
                                             abstract_segment_vector VECTOR(384)
);

-- Create an index on the title_vector and abstract_segment_vector columns for efficient similarity search
CREATE INDEX IF NOT EXISTS idx_title_vector ON paper_vectors USING ivfflat (title_vector);
CREATE INDEX IF NOT EXISTS idx_abstract_segment_vector ON paper_vectors USING ivfflat (abstract_segment_vector);

-- Add tsvector columns for abstract and title
ALTER TABLE papers ADD COLUMN abstract_tsv tsvector;
ALTER TABLE papers ADD COLUMN title_tsv tsvector;

-- Create indexes on the tsvector columns
CREATE INDEX idx_abstract_tsv ON papers USING gin(abstract_tsv);
CREATE INDEX idx_title_tsv ON papers USING gin(title_tsv);
