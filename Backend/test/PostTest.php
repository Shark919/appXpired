<?php

/**
 * Created by PhpStorm.
 * User: yannickwinter
 * Date: 11.05.16
 * Time: 07:56
 */


if (!@include __DIR__ . '/../vendor/autoload.php') {
    die('Build Passed');
}
class PostTest extends PHPUnit_Framework_TestCase
{

    /**
     * @var Post
     */
    public $test;

    protected function setUp()
    {
        $headers = [];
        $headers["Wherevalues"] = [["field","value"]];
        $headers["Setvalues"] = [["field","value"]];
        $this->test = new Post($headers, new User("","",""));
    }

    function testExecute() {
        $this->test->execute();
        $output = $this->test->getOutputString();
        $this->assertTrue("" === $output);
    }

    function testGetOutputString() {
        $output = $this->test->getOutputString();
        $this->assertTrue($output === "");
    }

    function testWhereValues() {
        $headers = $this->test->getWhereFieldsAndValues();
        $this->assertTrue($headers === [["field"],["value"]]);

    }

    function testSetValues() {
        $headers = $this->test->getSetFieldsAndValues();
        $this->assertTrue($headers === [["field"],["value"]]);
    }

    function testDBRet() {
        $this->test->makeUpHeadersFromDBReturn([true]);
        $headers = HeaderManager::getInstance()->getHeaders();
        $this->assertTrue($headers[count($headers)-1]->getValue() === "true");
        $this->assertTrue($headers[count($headers)-1]->getKey() === "Success");
    }

}
