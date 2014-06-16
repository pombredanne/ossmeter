module committersoverfile::Gini

import org::ossmeter::metricprovider::ProjectDelta;
import analysis::statistics::Frequency;
import analysis::statistics::Inference;
import IO;

@metric{committersoverfile}
@doc{Calculates the gini coefficient of committeroverfile}
@friendlyName{committersoverfile}
@appliesTo{generic()}
real giniCommittersOverFile(ProjectDelta delta = \empty()) {
  rel[str, str] filesCommitters = {< commitItem.path, vcC.author > | /VcsCommit vcC <- delta, commitItem <- vcC.items};

  committersOverFile = distribution(filesCommitters<1,0>);
  distCommitterOverFile = distribution(committersOverFile);
  
  return gini([<0,0>]+[<x, distCommitterOverFile[x]> | x <- distCommitterOverFile]);
}