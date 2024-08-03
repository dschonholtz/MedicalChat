import React, { useState } from 'react';

// If you're not using lucide-react, we'll create simple placeholder components
const ChevronDown = () => <span>▼</span>;
const ChevronUp = () => <span>▲</span>;
const Book = () => <span>📚</span>;

const PaperItem = ({ paper }) => {
    const [isExpanded, setIsExpanded] = useState(false);

    const toggleExpand = () => setIsExpanded(!isExpanded);

    return (
        <div className="bg-white border rounded-lg p-4 shadow-sm mb-4">
            <div className="flex items-start">
                <Book />
                <div className="flex-grow ml-3">
                    <h3 className="font-medium text-lg">{paper.title}</h3>
                    <p className="text-sm text-gray-600">{paper.authors}</p>
                    <p className="text-sm text-gray-500">Published: {paper.date}</p>
                    <p className="text-sm text-gray-500">Category: {paper.category}</p>
                    <button
                        onClick={toggleExpand}
                        className="mt-2 text-blue-500 hover:text-blue-700 flex items-center"
                    >
                        {isExpanded ? 'Hide Abstract' : 'Show Abstract'}
                        {isExpanded ? <ChevronUp /> : <ChevronDown />}
                    </button>
                </div>
            </div>
            {isExpanded && (
                <div className="mt-4 p-4 bg-gray-50 rounded">
                    <h4 className="font-medium mb-2">Abstract</h4>
                    <p className="text-sm">{paper.abstractText}</p>
                    <div className="mt-4 text-sm">
                        <p><strong>DOI:</strong> {paper.doi}</p>
                        <p><strong>Corresponding Author:</strong> {paper.authorCorresponding}</p>
                        <p><strong>Institution:</strong> {paper.authorCorrespondingInstitution}</p>
                        <p><strong>License:</strong> {paper.license}</p>
                    </div>
                </div>
            )}
        </div>
    );
};

export { PaperItem };