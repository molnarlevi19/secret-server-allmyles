import { useState } from 'react';

const SecretRetriever = () => {
    const [hash, setHash] = useState('');
    const [secretText, setSecretText] = useState('');
    const [error, setError] = useState('');

    const handleRetrieve = async () => {
        try {
            const response = await fetch(`/api/secrets/secret/${hash}`);
            if (response.ok) {
                const data = await response.text();
                setSecretText(data);
                setError('');
            } else if (response.status === 404) {
                setSecretText(null);
                setError('Secret not found');
            } else {
                setSecretText('');
                setError('Secret not found or cannot be retrieved.');
            }
        } catch (error) {
            console.error('Error retrieving secret:', error);
            setSecretText('');
            setError('Secret not found');
        }
    };

    return (
        <div>
            <h2>Secret Retriever</h2>
            <label>
                Enter Hash:
                <input type="text" value={hash} onChange={(e) => setHash(e.target.value)} />
            </label>
            <br />
            <button type="button" onClick={handleRetrieve}>
                Retrieve Secret
            </button>
            <br />
            {secretText && <p>Secret: {secretText}</p>}
            {error && <p>Error: {error}</p>}
        </div>
    );
};

export default SecretRetriever;
