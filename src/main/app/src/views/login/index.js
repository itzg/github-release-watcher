import React, {Component} from 'react';
import LoginRegister, {PROVIDER_GITHUB} from 'react-mui-login-register';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import PropTypes from 'prop-types';
import {withStyles} from '@material-ui/core/styles';

const styles = theme => ({
  root: {
    margin: theme.spacing.unit * 2
  }
});

class LoginView extends Component {
  static propTypes = {
    onLoginWithProvider: PropTypes.func.isRequired
  };

  render() {
    const {
      classes,
      onLoginWithProvider
    } = this.props;

    return (
      <div className={classes.root}>
        <LoginRegister providers={[PROVIDER_GITHUB]}
                       onLoginWithProvider={onLoginWithProvider}
                       disableLocal={true}
                       disableRegister={true}
                       header={
                         <AppBar position="static" color="primary">
                           <Toolbar>
                             <Typography variant="title" color="inherit">
                               GitHub Release Watcher
                             </Typography>
                           </Toolbar>
                         </AppBar>
                       }
        />
      </div>
    )
  }
}

export default withStyles(styles)(LoginView);