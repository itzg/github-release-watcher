import React, {Component} from 'react';
import {withStyles} from '@material-ui/core/styles';
import TableBody from "@material-ui/core/TableBody";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import Moment from "react-moment";
import PropTypes from 'prop-types';

const styles = theme => ({
  subDate: {
    color: theme.palette.text.hint
  }
});

class RepositoryListBody extends Component {
  static propTypes = {
    repositories: PropTypes.array.isRequired,
    sortBy: PropTypes.string,
    sortDirection: PropTypes.string
  };

  render() {
    const {
      repositories,
      classes
    } = this.props;

    let reposToMap = repositories
      .filter(repo => repo.releases && repo.releases.totalCount > 0); // creates a shallow copy of array

    if (this.props.sortBy) {
      reposToMap = reposToMap.sort(this.compareFunc);
    }

    return (
      <TableBody>
        {reposToMap.map(repo => {

            const repoOwnerName = repo.owner + '/' + repo.name;

            let mostRecentRelease = repo.releases.nodes[0];

            return (
              <TableRow key={repoOwnerName}>
                <TableCell><a href={repo.url+'/releases'} target='_blank'>{repoOwnerName}</a></TableCell>
                <TableCell>
                  <div>{mostRecentRelease.tag}</div>
                </TableCell>
                <TableCell>
                  <div>
                    <Moment date={mostRecentRelease.publishedAt} fromNow/>
                  </div>
                  <div className={classes.subDate}>
                    <Moment date={mostRecentRelease.publishedAt} format="LL"/>
                  </div>
                </TableCell>
              </TableRow>
            )

          })}
      </TableBody>
    );
  }

  compareFunc = (a, b) => {
    const {
      sortDirection,
      sortBy
    } = this.props;

    let valA, valB;
    switch (sortBy) {
      default:
      case 'repo':
        valA = (a.owner + '/' + a.name).toLowerCase();
        valB = (b.owner + '/' + b.name).toLowerCase();
        break;
      case 'released':
        valA = a.releases.nodes[0].publishedAt;
        valB = b.releases.nodes[0].publishedAt;
        break;
    }

    if (sortDirection === 'desc') {
      return valB < valA ? -1 : 1;
    }
    else {
      return valA < valB ? -1 : 1;
    }
  }
}

export default withStyles(styles)(RepositoryListBody);