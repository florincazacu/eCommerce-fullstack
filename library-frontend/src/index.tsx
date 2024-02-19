import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import {App} from './App';
import {BrowserRouter} from 'react-router-dom';
import {loadStripe} from "@stripe/stripe-js";
import {Elements} from "@stripe/react-stripe-js";

const stripePromise = loadStripe('pk_test_51Nt2u0GnOdiTeiP3fkh5u49y6Nkv8iL9OLm4B9dMXowBKbUwCoSVCf0y8Q1dKajnIJGmGYopLlqheYiNj8sYPVD600aGWcwopO');

const root = ReactDOM.createRoot(
	document.getElementById('root') as HTMLElement
);
root.render(
	<BrowserRouter>
		<Elements stripe={stripePromise}>
			<App/>
		</Elements>
	</BrowserRouter>
);