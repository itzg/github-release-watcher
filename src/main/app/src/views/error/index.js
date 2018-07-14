import React, {Component} from 'react';
import Card from '@material-ui/core/Card';
import CardHeader from '@material-ui/core/CardHeader';
import CardContent from '@material-ui/core/CardContent';
import {withStyles} from '@material-ui/core/styles';

const styles = theme => ({
  root: {
    margin: theme.spacing.unit * 2,
  },
  error: {
    color: theme.palette.error.main
  }
});

class ErrorView extends Component {

  render() {
    const {classes} = this.props;

    return <Card className={classes.root}>
      <CardHeader title="Error" classes={{
        title: classes.error
      }}/>
      <CardContent>{this.props.message}</CardContent>
    </Card>
  }
}

export default withStyles(styles)(ErrorView);