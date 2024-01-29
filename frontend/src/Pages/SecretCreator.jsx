import { useState } from 'react';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';

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
        <Container className="create-container mt-5 justify-content-center align-items-center">
            {hash ? (
                <div>
                    <p>Hash: {hash}</p>
                    <Button variant="primary" onClick={handleCopyToClipboard}>
                        Copy to Clipboard
                    </Button>
                    {copySuccess && <Alert variant="success">{copySuccess}</Alert>}
                </div>
            ) : (
                <div>
                    <h2>Secret Creator</h2>
                    <Form>
                        <Form.Group controlId="secretText">
                            <Form.Label>Secret Text:</Form.Label>
                            <Form.Control as="textarea" value={secretText} onChange={(e) => setSecretText(e.target.value)} />
                        </Form.Group>
                        <Form.Group controlId="maxRetrievals">
                            <Form.Label>Max Retrievals:</Form.Label>
                            <Form.Control type="number" value={maxRetrievals} onChange={(e) => setMaxRetrievals(parseInt(e.target.value, 10))} />
                        </Form.Group>
                        <Form.Group controlId="expiryTime">
                            <Form.Label>Expiry Time (in minutes):</Form.Label>
                            <Form.Control type="number" value={expiryTime} onChange={(e) => setExpiryTime(e.target.value)} />
                        </Form.Group>
                        <Button variant="primary" type="button" onClick={handleSave}>
                            Save Secret
                        </Button>
                    </Form>
                </div>
            )}
        </Container>
    );
};

export default SecretCreator;
