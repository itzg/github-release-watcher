import React, {Component} from 'react';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import Button from '@material-ui/core/Button';
import LinearProgress from '@material-ui/core/LinearProgress';
import {withStyles} from '@material-ui/core/styles';
import PropsTypes from 'prop-types';
import fetchWrapper from "../../fetchWrapper";
import RepositoryList from "./RepositoryList";

const styles = {
  root: {
    flexGrow: 1,
  },
  flex: {
    flex: 1,
  }
};

class MainView extends Component {
  static propTypes = {
    onLogout: PropsTypes.func.isRequired
  };

  state = {
    repositories: [],
    totalRepositories: 0,
    loadedRepositories: 0,
    myGithubUsername: null
  };

  render() {
    const {
      classes,
      onLogout
    } = this.props;

    const {
      myGithubUsername,
      totalRepositories,
      loadedRepositories
    } = this.state;

    return (
      <div className={classes.root}>

        <AppBar position="static">
          <Toolbar>
            <Typography variant="title" color="inherit" className={classes.flex}>
              GitHub Release Watcher
            </Typography>
            <Button color="inherit" onClick={onLogout}>{
              myGithubUsername ? 'Logout '+myGithubUsername : 'Logout'
            }</Button>
          </Toolbar>
        </AppBar>

        { totalRepositories > 0 && loadedRepositories < totalRepositories &&
          <LinearProgress variant="determinate" value={100.0 * (loadedRepositories/totalRepositories)}/>
        }
        <RepositoryList repositories={this.state.repositories}/>

      </div>
    )
  }


  componentDidMount() {
    fetchWrapper('/api/initialStarredRepos')
      .then(response => response.json())
      .then(content => {
        this.setState(prevState => ({
          repositories: [...prevState.repositories, ...content.nodes],
          totalRepositories: content.totalCount,
          loadedRepositories: prevState.loadedRepositories + content.nodes.length
        }));

        if (content.moreAvailable) {
          this.fetchNextStarredRepos(content.cursor);
        }
      });

    fetchWrapper('/api/githubUsername')
      .then(response => response.json())
      .then(content => {
        this.setState({myGithubUsername: content.value});
      });
  }

  fetchNextStarredRepos(cursor) {
    fetchWrapper(`/api/nextStarredRepos?cursor=${cursor}`)
      .then(response => response.json())
      .then(content => {
        this.setState(prevState => ({
          repositories: [...prevState.repositories, ...content.nodes],
          loadedRepositories: prevState.loadedRepositories + content.nodes.length
        }));

        if (content.moreAvailable) {
          this.fetchNextStarredRepos(content.cursor);
        }
      });
  }
}

export default withStyles(styles)(MainView);