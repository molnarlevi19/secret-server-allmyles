import { useRouteError } from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Alert from 'react-bootstrap/Alert';

const ErrorPage = () => {
    const error = useRouteError();

    return (
        <Container className="mt-5">
            <Alert variant="danger">
                <Alert.Heading>Oops!</Alert.Heading>
                <p>Sorry, an unexpected error has occurred.</p>
                <hr />
                <p className="mb-0">
                    <i>{error.statusText || error.message}</i>
                </p>
            </Alert>
        </Container>
    );
};

export default ErrorPage;