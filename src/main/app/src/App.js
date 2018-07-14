import React, {Component} from 'react';
import LinearProgress from '@material-ui/core/LinearProgress';
import CssBaseline from '@material-ui/core/CssBaseline';
import MainView from './views/main';
import LoginView from './views/login';
import ErrorView from './views/error';
import fetchWrapper from './fetchWrapper';

class App extends Component {
  state = {
    loggedIn: false,
    loading: true,
    appError: null,
  };

  handleProviderLogin = provider => {
    window.location.href = `/oauth2/authorization/${provider}`;
  };

  handleLogout = () => {
    this.setState({
      loading: true
    });

    fetch('/logout', {
      credentials: "same-origin",
      method: "POST"
    })
      .then(response => {
        if (response.ok) {
          this.loadStatus();
        }
      })
  };

  render() {
    return (
      <React.Fragment>
        <CssBaseline/>
        {this.currentView()}
      </React.Fragment>
    )
  }

  currentView() {
    if (this.state.appError) {
      return <ErrorView message={this.state.appError}/>;
    }

    if (this.state.loading) {
      return <LinearProgress/>;
    }

    if (!this.state.loggedIn) {
      return <LoginView onLoginWithProvider={this.handleProviderLogin}/>;
    }

    return <MainView onLogout={this.handleLogout}/>;
  }

  componentDidMount() {
    this.loadStatus();
  }

  loadStatus() {
    fetchWrapper('/api/status')
      .then(response => {
        return response.json();
      })
      .then(content => {
        this.setState({
          loggedIn: content.loggedIn,
          loading: false
        });
      })
      .catch(e => {
        this.setState({appError: e.response.statusText});
      })
  }
}

export default App;
