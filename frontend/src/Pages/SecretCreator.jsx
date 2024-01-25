import { useState } from 'react';

const SecretCreator = () => {
    const [secretText, setSecretText] = useState('');
    const [maxRetrievals, setMaxRetrievals] = useState(1);
    const [expiryTime, setExpiryTime] = useState('');
    const [hash, setHash] = useState('');
    const [copySuccess, setCopySuccess] = useState(null);

    const handleSave = async () => {

        const response = await fetch('/api/secrets/secret', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                secretText: secretText,
                remainingViews: maxRetrievals,
                expiryTimeInMinutes: expiryTime,
            }),
        });
        if (response.ok) {
            const data = await response.text();
            setHash(data);
            setCopySuccess(null);
        } else {
            console.error('Failed to save secret');
        }
    };

    const handleCopyToClipboard = async () => {
        try {
            await navigator.clipboard.writeText(hash);
            setCopySuccess('Copied to clipboard!');
        } catch (error) {
            console.error('Failed to copy to clipboard', error);
        }
    };

    return (
        <div>
            {hash ? (
                <div>
                    <p>Hash: {hash}</p>
                    <button type="button" onClick={handleCopyToClipboard}>
                        Copy to Clipboard
                    </button>
                    {copySuccess && <p style={{ color: 'green' }}>{copySuccess}</p>}
                </div>
            ) : (
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
            )}
        </div>
    );
};

export default SecretCreator;
