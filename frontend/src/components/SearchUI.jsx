import React, { useState } from 'react';
import { Search } from 'lucide-react';
import PaperItem from './Paper';

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
            if (!response.ok) throw new Error('Failed to fetch papers');
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
        <div className="max-w-4xl mx-auto p-6 space-y-8">
            <h1 className="text-4xl font-bold text-gray-800 text-center mb-8">Medical Research Assistant</h1>
            <form onSubmit={handleSearch} className="mb-8">
                <div className="flex items-center border-2 border-gray-300 rounded-full overflow-hidden focus-within:ring-2 focus-within:ring-blue-500 focus-within:border-transparent transition-all duration-200">
                    <input
                        type="text"
                        value={query}
                        onChange={(e) => setQuery(e.target.value)}
                        placeholder="Ask a medical research question..."
                        className="flex-grow p-4 outline-none text-gray-700"
                    />
                    <button type="submit" className="bg-blue-500 text-white p-4 hover:bg-blue-600 transition-colors duration-200" disabled={isLoading}>
                        <Search size={24} />
                    </button>
                </div>
            </form>

            {isLoading && <p className="text-center text-gray-600">Loading...</p>}
            {error && <p className="text-center text-red-500">{error}</p>}

            {papers.length > 0 && (
                <div>
                    <h2 className="text-2xl font-semibold mb-4 text-gray-800">Relevant Papers:</h2>
                    <ul className="space-y-6">
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