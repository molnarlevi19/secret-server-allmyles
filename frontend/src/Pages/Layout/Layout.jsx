import { Outlet, Link } from "react-router-dom";

import "./Layout.css";

const Layout = () => (
    <div className="Layout">
        <nav>
            <ul>
                <li className="grow">
                    <Link to="/">Secret Server</Link>
                </li>
                <li className="grow">
                    <Link to="/create">Create Secret</Link>
                </li>
                <li>
                    <Link to="/retrieve">
                        <button type="button">Retrieve Secret</button>
                    </Link>
                </li>
            </ul>
        </nav>
        <Outlet />
    </div>
);

export default Layout;
