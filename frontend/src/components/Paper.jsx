import React, { useState } from 'react';
import { ChevronDown, ChevronUp, Book } from 'lucide-react';

const PaperItem = ({ paper }) => {
    const [isExpanded, setIsExpanded] = useState(false);

    return (
        <div className="bg-white rounded-lg shadow-md overflow-hidden transition-all duration-300 ease-in-out hover:shadow-lg">
            <div className="p-6">
                <div className="flex items-start space-x-4">
                    <Book className="text-blue-500 flex-shrink-0" size={24} />
                    <div className="flex-grow">
                        <h3 className="text-xl font-semibold text-gray-800 mb-2">{paper.title}</h3>
                        <p className="text-sm text-gray-600 mb-1">{paper.authors}</p>
                        <p className="text-sm text-gray-500">Published: {paper.date}</p>
                        <p className="text-sm text-gray-500">Category: {paper.category}</p>
                    </div>
                </div>
                <button
                    onClick={() => setIsExpanded(!isExpanded)}
                    className="mt-4 text-blue-600 hover:text-blue-800 flex items-center transition-colors duration-200"
                >
                    {isExpanded ? 'Hide Abstract' : 'Show Abstract'}
                    {isExpanded ? <ChevronUp className="ml-1" /> : <ChevronDown className="ml-1" />}
                </button>
            </div>
            {isExpanded && (
                <div className="px-6 py-4 bg-gray-50 border-t border-gray-200">
                    <h4 className="font-semibold text-lg mb-2 text-gray-700">Abstract</h4>
                    <p className="text-gray-600 leading-relaxed mb-4 whitespace-pre-line">{paper.abstract}</p>
                    <div className="text-sm text-gray-500 space-y-1">
                        <p><span className="font-medium">DOI:</span> {paper.doi}</p>
                        <p><span className="font-medium">Corresponding Author:</span> {paper.authors}</p>
                        <p><span className="font-medium">Institution:</span> {paper.authorCorrespondingInstitution}</p>
                        <p><span className="font-medium">License:</span> {paper.license}</p>
                    </div>
                </div>
            )}
        </div>
    );
};

export default PaperItem;