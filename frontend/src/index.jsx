import React from 'react'
import ReactDOM from 'react-dom/client'
import { createBrowserRouter, RouterProvider } from "react-router-dom";

import './index.css'
import Layout from "./Pages/Layout/index.js";
import ErrorPage from "./Pages/ErrorPage.jsx";
import SecretCreator from "./Pages/SecretCreator.jsx";
import SecretRetriever from "./Pages/SecretRetriever.jsx";

const router = createBrowserRouter([
    {
        path: "/",
        element: <Layout />,
        errorElement: <ErrorPage />,
        children: [
            {
                path: "/create",
                element: <SecretCreator />,
            },
            {
                path: "/retrieve",
                element: <SecretRetriever />,
            }
        ],
    },
]);

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
    <React.StrictMode>
        <RouterProvider router={router} />
    </React.StrictMode>
);
