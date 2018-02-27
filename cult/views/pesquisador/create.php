<?php

use yii\helpers\Html;


/* @var $this yii\web\View */
/* @var $model app\models\Pesquisador */

$this->title = 'Adicionar pesquisador';
$this->params['breadcrumbs'][] = ['label' => 'pesquisador', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="pesquisador-create">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
