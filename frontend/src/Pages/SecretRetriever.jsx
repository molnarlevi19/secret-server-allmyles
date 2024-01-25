import { useState } from 'react';
import Container from 'react-bootstrap/Container';
import Form from 'react-bootstrap/Form';
import Button from 'react-bootstrap/Button';
import Alert from 'react-bootstrap/Alert';

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
        <Container className="retrieve-container mt-5">
            <h2>Secret Retriever</h2>
            <Form>
                <Form.Group controlId="hash">
                    <Form.Label>Enter Hash:</Form.Label>
                    <Form.Control type="text" value={hash} onChange={(e) => setHash(e.target.value)} />
                </Form.Group>
                <Button variant="primary" type="button" onClick={handleRetrieve}>
                    Retrieve Secret
                </Button>
            </Form>
            <br />
            {secretText && <p>Secret: {secretText}</p>}
            {error && <Alert variant="danger">Error: {error}</Alert>}
        </Container>
    );
};

export default SecretRetriever;
