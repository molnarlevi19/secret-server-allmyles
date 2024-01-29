// eslint-disable-next-line no-unused-vars
import React from 'react';
import { Outlet, Link } from "react-router-dom";
import Container from 'react-bootstrap/Container';
import Navbar from 'react-bootstrap/Navbar';

import "./Layout.css";

const Layout = () => (
    <Container fluid className="Layout">
        <Navbar expand="lg">
            <Navbar.Brand>
                <Link to="/" className="nav-link">
                    Home
                </Link>
            </Navbar.Brand>
            <Navbar.Brand>
                <Link to="/create" className="nav-link">
                    Create Secret
                </Link>
            </Navbar.Brand>
            <Navbar.Brand>
                <Link to="/retrieve" className="nav-link">
                    Retrieve Secret
                </Link>
            </Navbar.Brand>
        </Navbar>
        <Outlet />
    </Container>
);

export default Layout;
