import React, {Component} from 'react';
import TableCell from '@material-ui/core/TableCell';
import TableSortLabel from '@material-ui/core/TableSortLabel';
import PropTypes from 'prop-types';

class SortingHeaderCell extends Component {
  static propTypes = {
    sortBy: PropTypes.string,
    activeSortField: PropTypes.string,
    sortDirection: PropTypes.string,
    sortingUpdater: PropTypes.func
  };

  render() {
    const {
      sortBy,
      activeSortField,
      sortDirection,
      sortingUpdater
    } = this.props;
    
    return (
      <TableCell sortDirection={activeSortField === sortBy ? sortDirection : false}>
        <TableSortLabel active={activeSortField === sortBy}
                        direction={sortDirection}
                        onClick={sortingUpdater}>
          {this.props.children}
        </TableSortLabel>
      </TableCell>
    );
  }
}

export default SortingHeaderCell;