import React, { useState } from 'react';
import { Search } from 'lucide-react';
import { PaperItem } from './Paper'; // Make sure this path is correct

const SearchUI = () => {
    const [query, setQuery] = useState('');
    const [papers, setPapers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleSearch = async (e) => {
        e.preventDefault();
        setIsLoading(true);
        setError(null);
        try {
            const response = await fetch(`/papers/search?query=${encodeURIComponent(query)}`);
            if (!response.ok) {
                throw new Error('Failed to fetch papers');
            }
            const data = await response.json();
            setPapers(data);
        } catch (error) {
            console.error('Error fetching papers:', error);
            setError('Failed to fetch papers. Please try again.');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <div className="max-w-4xl mx-auto p-4">
            <h1 className="text-3xl font-bold mb-4">Medical Research Assistant</h1>
            <form onSubmit={handleSearch} className="mb-6">
                <div className="flex items-center border-2 rounded-lg">
                    <input
                        type="text"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        placeholder="Ask a medical research question..."
                        className="flex-grow p-2 outline-none"
                    />
                    <button type="submit" className="bg-blue-500 text-white p-2 rounded-r-lg hover:bg-blue-600 transition-colors" disabled={isLoading}>
                        <Search size={24} />
                    </button>
                </div>
            </form>

            {isLoading && <p>Loading...</p>}
            {error && <p className="text-red-500">{error}</p>}

            {papers.length > 0 && (
                <div className="mb-6">
                    <h2 className="text-xl font-semibold mb-2">Relevant Papers:</h2>
                    <ul className="space-y-4">
                        {papers.map(paper => (
                            <li key={paper.doi || paper.id}>
                                <PaperItem paper={paper} />
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default SearchUI;