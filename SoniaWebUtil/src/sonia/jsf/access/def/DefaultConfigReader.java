/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



package sonia.jsf.access.def;

//~--- non-JDK imports --------------------------------------------------------

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import sonia.jsf.access.Action;
import sonia.jsf.access.Condition;
import sonia.jsf.access.def.action.ForwardAction;
import sonia.jsf.access.def.action.MessageAction;
import sonia.jsf.access.def.action.NavigationAction;
import sonia.jsf.access.def.action.RedirectAction;
import sonia.jsf.access.def.condition.AndCondition;
import sonia.jsf.access.def.condition.ContainerCondition;
import sonia.jsf.access.def.condition.IsUserCondition;
import sonia.jsf.access.def.condition.IsUserInRoleCondition;
import sonia.jsf.access.def.condition.LoggedInCondition;
import sonia.jsf.access.def.condition.NotCondition;
import sonia.jsf.access.def.condition.OrCondition;
import sonia.jsf.access.def.condition.UriMatchesCondition;
import sonia.jsf.access.def.condition.ViewIdMatchesCondition;

import sonia.util.Util;
import sonia.util.XmlUtil;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 *
 * @author sdorra
 */
public class DefaultConfigReader
{

  /** Field description */
  private static final String SCHEMA = "/sonia/jsf/access/accessConfig.xsd";

  //~--- methods --------------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param in
   *
   * @return
   *
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public List<Rule> readConfig(InputStream in)
          throws IOException, SAXException, ParserConfigurationException
  {
    List<Rule> ruleList = new ArrayList<Rule>();
    InputStream schema = Util.findResource(SCHEMA);
    Document doc = XmlUtil.buildDocument(in, schema);
    NodeList ruleNodeList = doc.getElementsByTagName("rule");

    if (ruleNodeList != null)
    {
      for (int i = 0; i < ruleNodeList.getLength(); i++)
      {
        Node ruleNode = ruleNodeList.item(i);

        if ((ruleNode != null) && ruleNode.getNodeName().equals("rule"))
        {
          Rule rule = buildRule(ruleNode);

          if (rule != null)
          {
            ruleList.add(rule);
          }
        }
      }
    }

    return ruleList;
  }

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   */
  private List<Action> buildActions(Node node)
  {
    List<Action> actionList = new ArrayList<Action>();
    NodeList children = node.getChildNodes();

    for (int i = 0; i < children.getLength(); i++)
    {
      Node child = children.item(i);
      String childName = child.getNodeName();

      if (childName.equals("redirect"))
      {
        String target = child.getTextContent();

        if (!isBlank(target))
        {
          actionList.add(new RedirectAction(target));
        }
      }
      else if (childName.equals("forward"))
      {
        String target = child.getTextContent();

        if (!isBlank(target))
        {
          actionList.add(new ForwardAction(target));
        }
      }
      else if (childName.equals("jsf-navigation"))
      {
        String target = child.getTextContent();

        if (!isBlank(target))
        {
          actionList.add(new NavigationAction(target));
        }
      }
      else if (childName.equals("jsf-message"))
      {
        String message = child.getTextContent();

        if (!isBlank(message))
        {
          actionList.add(new MessageAction(message));
        }
      }
      else if (childName.equals("action"))
      {
        Action action = buildCustomAction(child);

        if (action != null)
        {
          actionList.add(action);
        }
      }
    }

    return actionList;
  }

  /**
   * Method description
   *
   *
   * @param node
   * @param parent
   *
   * @return
   */
  private Condition buildCondition(Node node, ContainerCondition parent)
  {
    NodeList children = node.getChildNodes();

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);
        String childName = child.getNodeName();

        if (childName.equals("isUser"))
        {
          String username = child.getTextContent();

          if (!isBlank(username))
          {
            parent.add(new IsUserCondition(username));
          }
        }
        else if (childName.equals("isUserInRole"))
        {
          String roleName = child.getTextContent();

          if (!isBlank(roleName))
          {
            parent.add(new IsUserInRoleCondition(roleName));
          }
        }
        else if (childName.equals("ViewIdMatches"))
        {
          String regex = child.getTextContent();

          if (!isBlank(regex))
          {
            parent.add(new ViewIdMatchesCondition(regex));
          }
        }
        else if (childName.equals("uriMatches"))
        {
          String regex = child.getTextContent();

          if (!isBlank(regex))
          {
            parent.add(new UriMatchesCondition(regex));
          }
        }
        else if (childName.equals("loggedIn"))
        {
          parent.add(new LoggedInCondition());
        }
        else if (childName.equals("and"))
        {
          Condition andCondition = buildCondition(child, new AndCondition());

          if (andCondition != null)
          {
            parent.add(andCondition);
          }
        }
        else if (childName.equals("or"))
        {
          Condition orCondition = buildCondition(child, new OrCondition());

          if (orCondition != null)
          {
            parent.add(orCondition);
          }
        }
        else if (childName.equals("not"))
        {
          Condition notCondtion = buildCondition(child, new NotCondition());

          if (notCondtion != null)
          {
            parent.add(notCondtion);
          }
        }
        else if (childName.equals("condition"))
        {
          Condition condition = buildCustomCondition(child);

          if (condition != null)
          {
            parent.add(condition);
          }
        }
      }
    }

    return parent;
  }

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   */
  private Action buildCustomAction(Node node)
  {
    Action action = null;
    Map<String, String> parameters = new HashMap<String, String>();
    NodeList children = node.getChildNodes();

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("class"))
        {
          action = createInstance(Action.class, child.getTextContent());
        }
        else if (child.getNodeName().equals("init-param"))
        {
          parseInitParam(parameters, node);
        }
      }
    }

    if (action != null)
    {
      action.init(parameters);
    }

    return action;
  }

  /**
   * Method description
   *
   *
   * @param node
   *
   * @return
   */
  private Condition buildCustomCondition(Node node)
  {
    Condition condition = null;
    Map<String, String> parameters = new HashMap<String, String>();
    NodeList children = node.getChildNodes();

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("class"))
        {
          String classString = child.getTextContent();

          condition = createInstance(Condition.class, classString);
        }
        else if (child.getNodeName().equals("init-param"))
        {
          parseInitParam(parameters, child);
        }
      }
    }

    if (condition != null)
    {
      condition.init(parameters);
    }

    return condition;
  }

  /**
   * Method description
   *
   *
   * @param ruleNode
   *
   * @return
   */
  private Rule buildRule(Node ruleNode)
  {
    Rule rule = new Rule();
    NodeList children = ruleNode.getChildNodes();

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("conditions"))
        {
          Condition condition = buildCondition(child, new AndCondition());

          if (condition != null)
          {
            rule.setCondition(condition);
          }
        }
        else if (child.getNodeName().equals("actions"))
        {
          List<Action> actions = buildActions(child);

          if (actions != null)
          {
            rule.setActions(actions);
          }
        }
        else if (child.getNodeName().equals("last"))
        {
          rule.setLast(true);
        }
      }
    }

    return rule;
  }

  /**
   * Method description
   *
   *
   * @param clazz
   * @param classString
   * @param <T>
   *
   * @return
   */
  private <T> T createInstance(Class<T> clazz, String classString)
  {
    T instance = null;

    try
    {
      Object obj = Class.forName(classString).newInstance();

      instance = clazz.cast(obj);
    }
    catch (Exception ex)
    {
      ex.printStackTrace(System.err);
    }

    return instance;
  }

  /**
   * Method description
   *
   *
   * @param parameters
   * @param node
   */
  private void parseInitParam(Map<String, String> parameters, Node node)
  {
    String name = null;
    String value = null;
    NodeList children = node.getChildNodes();

    if (children != null)
    {
      for (int i = 0; i < children.getLength(); i++)
      {
        Node child = children.item(i);

        if (child.getNodeName().equals("param-name"))
        {
          name = child.getTextContent();
        }
        else if (child.getNodeName().equals("param-value"))
        {
          value = child.getTextContent();
        }
      }
    }

    if ((name != null) && (value != null))
    {
      parameters.put(name, value);
    }
  }

  //~--- get methods ----------------------------------------------------------

  /**
   * Method description
   *
   *
   * @param value
   *
   * @return
   */
  private boolean isBlank(String value)
  {
    return (value == null) || (value.trim().length() == 0);
  }
}
