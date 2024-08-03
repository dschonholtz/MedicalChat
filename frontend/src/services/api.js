// src/services/api.js

const API_BASE_URL = process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080/api';

export const searchPapers = async (query, options = {}) => {
    const {
        titleWeight = 1.0,
        abstractWeight = 1.0,
        titleRankWeight = 1.0,
        abstractRankWeight = 1.0,
        keywordCountWeight = 1.0,
        topK = 10
    } = options;

    const url = new URL(`${API_BASE_URL}/papers/search`);
    url.search = new URLSearchParams({
        query,
        titleWeight,
        abstractWeight,
        titleRankWeight,
        abstractRankWeight,
        keywordCountWeight,
        topK
    }).toString();

    console.log('Constructed URL:', url.toString());

    try {
        const response = await fetch(url);
        console.log('Response Status:', response.status);
        console.log('Response Headers:', JSON.stringify([...response.headers]));

        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const responseBody = await response.json();
        console.log('Response Body:', JSON.stringify(responseBody));
        return responseBody;
    } catch (error) {
        console.error('Error fetching papers:', error);
        throw error;
    }
};