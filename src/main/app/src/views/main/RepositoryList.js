import React, {Component} from 'react';
import PropTypes from 'prop-types';
import Table from '@material-ui/core/Table';
import TableHead from '@material-ui/core/TableHead';
import TableRow from '@material-ui/core/TableRow';
import TableCell from '@material-ui/core/TableCell';
import SortingHeaderCell from "./SortingHeaderCell";
import RepositoryListBody from "./RepositoryListBody";

class RepositoryList extends Component {
  static propTypes = {
    repositories: PropTypes.array.isRequired
  };

  state = {
    sortBy: null, // or repo or released
    sortDirection: 'desc'
  };

  render() {
    const {
      repositories,
    } = this.props;

    const {
      sortBy,
      sortDirection
    } = this.state;

    return (
      <div>

        <Table>
          <TableHead>
            <TableRow>
              <SortingHeaderCell sortBy='repo'
                                 activeSortField={sortBy}
                                 sortDirection={sortDirection}
                                 sortingUpdater={this.createSortingUpdater('repo')}>
                Repository
              </SortingHeaderCell>
              <TableCell>
                Release
              </TableCell>
              <SortingHeaderCell sortBy='released'
                                 activeSortField={sortBy}
                                 sortDirection={sortDirection}
                                 sortingUpdater={this.createSortingUpdater('released')}>
                Released
              </SortingHeaderCell>
            </TableRow>
          </TableHead>

          <RepositoryListBody repositories={repositories} sortBy={sortBy} sortDirection={sortDirection}/>

        </Table>

      </div>
    )
  }

  createSortingUpdater = field => () => {
    this.setState(prevState => {
      if (field === prevState.sortBy) {
        return {
          sortDirection: prevState.sortDirection === 'asc' ? 'desc' : 'asc'
        };
      }

      let newState = {
        sortBy: field
      };

      switch (field) {
        case 'repo':
          newState.sortDirection = 'asc';
          break;
        case 'released':
          newState.sortDirection = 'desc';
          break;
        default:
          newState.sortDirection = null;
          break;
      }

      return newState;
    });
  }
}

export default RepositoryList;