import { useState } from 'react';

const SecretCreator = () => {
    const [secretText, setSecretText] = useState('');
    const [maxRetrievals, setMaxRetrievals] = useState(1);
    const [expiryTime, setExpiryTime] = useState('');

    const handleSave = async () => {

        const response = await fetch('http://localhost:8080/api/secrets/secret', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                text: secretText,
                remainingViews: maxRetrievals,
                expiryTimeInMinutes: expiryTime,
            }),
        });
        if (response.ok) {
            const data = await response.text();
            console.log('Secret saved with hash:', data);
        } else {
            console.error('Failed to save secret');
        }
    };

    return (
        <div>
            <h2>Secret Creator</h2>
            <form>
                <label>
                    Secret Text:
                    <textarea value={secretText} onChange={(e) => setSecretText(e.target.value)} />
                </label>
                <br />
                <label>
                    Max Retrievals:
                    <input
                        type="number"
                        value={maxRetrievals}
                        onChange={(e) => setMaxRetrievals(parseInt(e.target.value, 10))}
                    />
                </label>
                <br />
                <label>
                    Expiry Time (in minutes):
                    <input
                        type="number"
                        value={expiryTime}
                        onChange={(e) => setExpiryTime(e.target.value)}
                    />
                </label>
                <br />
                <button type="button" onClick={handleSave}>
                    Save Secret
                </button>
            </form>
        </div>
    );
};

export default SecretCreator;
