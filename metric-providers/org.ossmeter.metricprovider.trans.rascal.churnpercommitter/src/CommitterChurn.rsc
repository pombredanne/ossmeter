module CommitterChurn

import org::ossmeter::metricprovider::Manager;
import org::ossmeter::metricprovider::ProjectDelta;

@metric{churnPerCommitter}
@doc{Count churn per committer}
@friendlyName{Counts number of lines added and deleted per committer}
@appliesTo{generic()}
map[str author, int churn] churnPerCommitter(ProjectDelta delta = \empty())
  = (co.author : churn(co) | /VcsCommit co := delta)
  ;
  
int churn(VcsCommit item) 
  = (0 | it + count | /linesAdded(count) := item)
  + (0 | it + count | /linesDeleted(count) := item)
  ;