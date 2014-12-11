/*******************************************************************************
 * Copyright (c) 2014 OSSMETER Partners.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Jacob Carter - Implementation.
 *******************************************************************************/
package org.ossmeter.platform.bugtrackingsystem.redmine.api;

import java.io.IOException;

import org.ossmeter.jackson.ExtendedJsonDeserialiser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;

class RedmineIssueDeserialiser extends
		ExtendedJsonDeserialiser<RedmineIssue> {

	@Override
	public RedmineIssue deserialize(JsonParser parser,
			DeserializationContext context) throws IOException,
			JsonProcessingException {
		ObjectCodec oc = parser.getCodec();
		JsonNode node = oc.readTree(parser);

		RedmineIssue issue = new RedmineIssue();

		String bugId = getText(node, "id");

		issue.setBugId(bugId);
		issue.setCategory(getText(node, "category", "name"));
		issue.setCategoryId(getInteger(node, "category", "id"));
		issue.setCreationTime(getDate(node, context, "created_on"));
		issue.setCreator(getText(node, "author", "name"));
		issue.setCreatorId(getInteger(node, "author", "id"));
		issue.setDescription(getText(node, "description"));
		issue.setPriority(getText(node, "priority", "name"));
		issue.setPriorityId(getInteger(node, "priority", "id"));
		issue.setProject(getText(node, "project", "name"));
		issue.setProjectId(getInteger(node, "project", "id"));
		issue.setStatus(getText(node, "status", "name"));
		issue.setStatusId(getInteger(node, "status", "id"));
		issue.setSubject(getText(node, "subject"));
		issue.setTracker(getText(node, "tracker", "name"));
		issue.setTrackerId(getInteger(node, "tracker", "id"));
		issue.setUpdateDate(getDate(node, context, "updated_on"));

		JsonNode commentsNode = node.get("journals");
		if (null != commentsNode) {
			RedmineComment[] comments = oc.treeToValue(commentsNode,
					RedmineComment[].class);
			for (RedmineComment comment : comments) {
				issue.getComments().add(comment);
				comment.setBugId(bugId);
			}
		}

		return issue;
	}

}
